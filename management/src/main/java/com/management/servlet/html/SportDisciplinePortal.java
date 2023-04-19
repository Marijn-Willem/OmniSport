package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class SportDisciplinePortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sportdiscipline");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"sportDisciplineLoader.loadElement();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        int spid = getIntValuedParameterValue(req, "spid");
        writeVarInScriptTag("spid", spid, w);
        writeInitStateVarInScriptTag("did", req, w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportPortal?spid=" + getIntValuedParameterValue(req, "spid");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        int spid = getIntValuedParameterValue(req, "spid");

        w.append("<select id=\"did\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageSportDiscipline();\" value=\"Manage Sport Discipline\" />\n");
        w.append("<input type=\"button\" onclick=\"goToDisciplinePartPortal();\" value=\"Manage Discipline Parts\" />\n");
        w.append("<div>\n<a href=\"");
        w.append(path);
        w.append("/ManageSportDiscipline?spid=");
        w.append(Integer.toString(spid));
        w.append("&md=i\">Add Sport Discipline</a>\n</div>\n");
    }
}
