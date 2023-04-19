package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class CompSeasonPortal extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected int getSportId(HttpServletRequest req) {
        return getIntValuedParameterValue(req, "spid");
    }

    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("compseason");

        initSpecificProperties(req);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initCompSeason();\">");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        w.append("const spid = ");
        w.append(Integer.toString(getSportId(req)));
        w.append(";\n");
        writeInitStateVarInScriptTag("cid", req, w);
        writeInitStateVarInScriptTag("sid", req, w);
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<div>\n");
        w.append("<select id=\"selCid\" onchange=\"seasonLoader.loadElement();\">\n</select><br/>\n");
        w.append("<select id=\"selSid\" onchange=\"detailLoader.loadElement();\">\n</select>\n");
        w.append("</div>\n");
        w.append("<div id=\"divPorts\">\n</div>\n");
    }
}
