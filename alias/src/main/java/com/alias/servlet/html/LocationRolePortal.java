package com.alias.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class LocationRolePortal extends SuperHtmlServlet {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadLocationRoleList();\">\n");
    }

    @Override
    void processSpecific(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"lrid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAliasPortal(getLocationRoleId);\" value=\"Manage Aliases\" /><br/>");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }
}
