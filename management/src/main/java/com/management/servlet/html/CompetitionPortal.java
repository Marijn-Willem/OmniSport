package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompetitionPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("competition");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadCompetitionList();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException, SQLException {
        writeInitStateVarInScriptTag("cid", req, w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"selCid\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageCompetition();\" value=\"Manage Competition\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToCompSeasonManagementPortal();\" value=\"Manage Competition Seasons\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToCompDivisionPortal();\" value=\"Manage Competition Divisions\" /><br/>\n");
        w.append("<div>\n");
        w.append("<a href=\"");
        w.append(path);
        w.append("/ManageCompetition?md=i\">Add Competition</a>\n");
        w.append("</div>\n");
    }
}
