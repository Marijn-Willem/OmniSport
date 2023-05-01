package com.sportservlet.html;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.*;
import com.sports.logic.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class ManageCompSeasonEventPart extends ManageEntity {
    private SportEventKey sportEventKey;
    private boolean hasFixedParts;

    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("compseasoneventpart");
    }

    @Override
    protected String getEntityIdName() {
        return "csepid";
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);

        int cseid = getIntValuedParameterValue(req, "cseid");
        CompSeasonEventKey cseKey = new CompSeasonEventKey(compSeasonKey, cseid);
        CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);
        sportEventKey = cse.getSportEventKey();
        hasFixedParts = new DbCalculation(stat).hasSportEventParts(sportEventKey);

        writeCompSeasonEventVarsInScriptTag(req, w);
        w.append("const fp = ");
        w.append(Boolean.toString(hasFixedParts));
        w.append(";\n");
    }

    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int cseid = getIntValuedParameterValue(req, "cseid");

        CompSeasonEventPart csep = null;
        if (!"i".equals(mode)) {
            int csepid = getIntValuedParameterValue(req, "csepid");
            CompSeasonEventPartKey csepk = new CompSeasonEventPartKey(
                    new CompSeasonEventKey(compSeasonKey, cseid), csepid);

            csep = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepk);
        }

        List<SportEventPart> sportEventParts = new SportEventPartManager(stat).getSportEventParts(sportEventKey);
        LinkedHashMap<Integer, String> sportEventPartMap = getLinkedHashMapFromNamedEntities(sportEventParts, true,
                SportEventPart::getSportEventPartId);

        List<SportDiscipline> sportDisciplines = new SportDisciplineManager(stat).getSportDisciplinesForSport(
                sportEventKey.getSportId());
        LinkedHashMap<Integer, String> sportDisciplineMap = getLinkedHashMapFromNamedEntities(sportDisciplines, true,
                SportDiscipline::getSportDisciplineId);

        List<EventPartName> eventPartNames = new EventPartNameManager(stat).getEventPartNames();
        LinkedHashMap<Integer, String> eventPartNameMap = getLinkedHashMapFromNamedEntities(eventPartNames, true,
                EventPartName::getId);

        Writer w = res.getWriter();

        writeSelectWithLabel("Sport Event Part", "epid", sportEventPartMap, csep != null ? csep.getSportEventPartId() : null, hasFixedParts, w);
        writeSelectWithLabel("Sport Discipline", "did", sportDisciplineMap, csep != null ? csep.getSportDisciplineId() : null, hasFixedParts, w);
        writeSelectWithLabel("Event part name", "epnid", eventPartNameMap, csep != null ? csep.getEventPartNameId() : null, hasFixedParts, w);
        writeNumericTextField("Order", "o", csep != null ? csep.getOrder() : null, hasFixedParts, w);
        writeNumericTextField("Stage", "st", csep != null ? csep.getStage() : null, hasFixedParts, w);
        writeDateTimeField("Date", "dt", csep != null ? csep.getDate() : null, w);
        writeTextFieldWithLabel("External source", "es", csep != null ? csep.getExternalSource() : null, w);
    }

    protected String getUpdateId(HttpServletRequest req) {
        return Util.convertNullStringToEmpty(req.getParameter("csepid"));
    }
}
