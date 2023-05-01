package com.sportservlet.html;

import com.sports.calc.alcifo.DbCalculation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class EventDisciplinePartPortal extends SuperHtmlServlet implements AbstractHtmlServlet {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"init();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w)
            throws IOException, SQLException {
        boolean isTeam = new DbCalculation(stat).getSportEvent(getCompSeasonEventKey(req)).isTeam();

        writeCompSeasonEventPartVarsInScriptTag(req, w);
        w.append("const isTeam = ");
        w.append(Boolean.toString(isTeam));
        w.append(";\n");
    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("eventdisciplinepart");
        initSpecificProperties(req);
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<div>\n<select id=\"edpid\" onchange=\"loadEventDisciplinePartPorts();\">\n</select>\n</div>\n");
        w.append("<div id=\"divPorts\">\n</div>\n");
        writeSpecificPart(stat, req, w);
    }

    protected void writeSpecificPart(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException { }
}
