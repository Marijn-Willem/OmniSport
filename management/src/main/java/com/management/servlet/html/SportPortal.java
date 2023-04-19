package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class SportPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("sport");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"sportLoader.loadElement();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        writeInitStateVarInScriptTag("spid", req, w);
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<select id=\"selSpid\" onchange=\"handleSelectSport();\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageSport();\" value=\"Manage Sport\" />\n<br/>\n");
        w.append("<input id=\"btnSe\" type=\"button\" onclick=\"goToSportEventPortal();\" value=\"Manage Sport Events\" /><br/>\n");
        w.append("<input id=\"btnSd\" type=\"button\" onclick=\"goToSportDisciplinePortal();\" value=\"Manage Sport Disciplines\" /><br/>\n");
        w.append("<div>\n");
        w.append("<a href=\"");
        w.append(path);
        w.append("/ManageSport?md=i\">Add Sport</a>\n");
        w.append("</div>\n");
    }
}
