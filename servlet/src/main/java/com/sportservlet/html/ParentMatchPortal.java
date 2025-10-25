package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public abstract class ParentMatchPortal extends SuperHtmlServlet implements AbstractHtmlServlet {
    protected boolean showAddChildMatch() {
        return true;
    }

    @Override
    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("parentmatch");
        initSpecificProperties(req);
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("pid", getIntValuedParameterValue(req, "pid"), w);
        writeVarInScriptTag("pmid", getIntValuedParameterValue(req, "pmid"), w);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadChildMatches();\">\n");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<table id=\"tblChildMatch\" border=\"1\">\n");
        w.append("</table>\n");
        if (showAddChildMatch())
            w.append("<input type=\"button\" onclick=\"goToAddChildMatch();\" value=\"Add Child match\" /><br/>\n");
    }
}
