package com.sportservlet.html;

import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.SportEventManager;
import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class CompSeasonEventPartPortal extends SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("compseasoneventpart");
        initSpecificProperties(req);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initPortal();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w)
            throws IOException, SQLException {
        int spid = new DbCalculation(stat).getSportId(competitionId);
        int eid = getIntValuedParameterValue(req, "eid");
        boolean isTeam = new SportEventManager(stat).getEntityFromSuperKey(new SportEventKey(spid, eid)).isTeam();

        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("eid", eid, w);
        w.append("const isTeam = ");
        w.append(Boolean.toString(isTeam));
        w.append(";\n");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<div>\n<select id=\"csepid\" onchange=\"loadPorts();\">\n</select>\n</div>");
        w.append("<div id=\"divPorts\">\n</div>\n");
    }
}
