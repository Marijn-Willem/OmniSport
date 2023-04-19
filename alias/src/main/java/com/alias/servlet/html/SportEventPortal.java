package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class SportEventPortal extends SuperHtmlServlet {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadAlcifoSportList();\">\n");
    }

    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"spid\" onchange=\"loadSportEventList();\">\n</select><br/>\n");
        w.append("<select id=\"eid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getSportEventKey);\" value=\"Manage Aliases\" /><br/>\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }
}
