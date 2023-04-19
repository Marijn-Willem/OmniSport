package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class SeasonPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("season");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadSeasonList();\">");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"sid\">\n</select>\n<br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageSeason();\" value=\"Manage Season\" />\n");
        w.append("<div>\n");
        w.append("<a href=\"");
        w.append(path);
        w.append("/ManageSeason?md=i\">Add Season</a>\n");
        w.append("</div>\n");
    }
}
