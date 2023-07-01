package com.sportservlet.html;

import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.DisciplinePart;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.SportDiscipline;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.DisciplinePartKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.DisciplinePartManager;
import com.sports.entity.manager.EventDisciplinePartManager;
import com.sports.entity.manager.SportDisciplineManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageEventDisciplinePart extends ManageEntity {
    @Override
    protected void initSpecific(Statement stat, HttpServletRequest req) {}

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("eventdisciplinepart");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("cseid", getIntValuedParameterValue(req, "cseid"), w);
        writeVarInScriptTag("csepid", getIntValuedParameterValue(req, "csepid"), w);
    }

    @Override
    protected String getEntityIdName() {
        return "edpid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        CompSeasonEventPartKey csepKey = getCompSeasonEventPartKey(req);

        EventDisciplinePart eventDisciplinePart = null;
        if (!"i".equals(mode)) {
            int edpid = getIntValuedParameterValue(req, "edpid");
            EventDisciplinePartKey edpKey = new EventDisciplinePartKey(csepKey, edpid);

            eventDisciplinePart = new EventDisciplinePartManager(stat).getEntityFromSuperKey(edpKey);
        }

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(csepKey);
        SportDisciplineKey sdk = compSeasonEventPart.getSportDisciplineKey();
        SportDiscipline sportDiscipline = new SportDisciplineManager(stat).getEntityFromSuperKey(sdk);

        DisciplinePartKey dpk = eventDisciplinePart != null && eventDisciplinePart.getDisciplinePartId() != null ?
                new DisciplinePartKey(sdk, eventDisciplinePart.getDisciplinePartId()) : null;
        DisciplinePart disciplinePart = dpk != null ? new DisciplinePartManager(stat).getDisciplinePart(dpk) : null;

        Writer w = res.getWriter();

        writeSpanWithLabel("Sport discipline", sportDiscipline.getName(), w);
        writeSpanWithLabel("Discipline part", disciplinePart != null ? disciplinePart.getName() : null, w);
        writeTextFieldWithLabel("Name", "nm", eventDisciplinePart != null ? eventDisciplinePart.getName() : null, w);
    }
}
