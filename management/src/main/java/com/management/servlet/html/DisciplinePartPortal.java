package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class DisciplinePartPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("disciplinepart");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"disciplinePartLoader.loadElement();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("spid", getIntValuedParameterValue(req, "spid"), w);
        writeVarInScriptTag("did", getIntValuedParameterValue(req, "did"), w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportDisciplinePortal?spid=" + getIntValuedParameterValue(req, "spid") +
                "&did=" + getIntValuedParameterValue(req, "did");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        int spid = getIntValuedParameterValue(req, "spid");
        int did = getIntValuedParameterValue(req, "did");

        w.append("<select id=\"dpid\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageDisciplinePart();\" value=\"Manage Discipline Part\" />\n");
        w.append("<div>\n<a href=\"");
        w.append(path);
        w.append("/ManageDisciplinePart?spid=");
        w.append(Integer.toString(spid));
        w.append("&did=");
        w.append(Integer.toString(did));
        w.append("&md=i\">Add Discipline Part</a>\n</div>\n");
    }
}
