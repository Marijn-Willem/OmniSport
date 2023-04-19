package com.sportservlet.html;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.CompetitionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageCompSeasonEvent extends ManageEntity {
    private int spid;

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        super.processScriptTag(stat, req, w);
        writeCompSeasonVarsInScriptTag(w);
        spid = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
        writeVarInScriptTag("spid", spid, w);
    }

    @Override
    protected void initAbstractProperties(HttpServletRequest req) {
        jsList.add("compseasonevent");
    }

    @Override
    protected String getEntityIdName() {
        return "eid";
    }

    @Override
    protected void processSpecific(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        int eid = getIntValuedParameterValue(req, "eid");
        CompSeasonEventKey cseKey = new CompSeasonEventKey(compSeasonKey, spid, eid);
        CompSeasonEvent cse = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);

        Writer w = res.getWriter();

        writeTextFieldWithLabel("External source", "es", cse != null ? cse.getExternalSource() : null, w);
    }
}
