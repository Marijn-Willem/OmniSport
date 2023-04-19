package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class GeoTypePortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("geotype");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initPortal();\">\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "GeoPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<div>\n<select id=\"gtid\">\n</select>\n</div>\n");
        w.append("<input type=\"button\" value=\"Manage GeoType\" onclick=\"goToManageGeoType();\" /><br/>\n");
        w.append("<input type=\"button\" value=\"Add GeoType\" onclick=\"goToAddGeoType();\" /><br/>\n");
    }
}
