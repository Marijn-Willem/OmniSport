package com.sportservlet.html;

import com.sports.calc.alcifo.DbCalculation;
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
        boolean isTeam = new DbCalculation(stat).getSportEvent(getCompSeasonEventKey(req)).isTeam();

        writeCompSeasonEventVarsInScriptTag(req, w);
        writeInitStateVarInScriptTag("csepid", req, w);
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
