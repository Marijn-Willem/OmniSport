package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class NoCountResultPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("nocountresult");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initPortal();\">\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<select id=\"ncrid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageNoCountResult();\" value=\"Manage NoCount Result\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToInsertNoCountResult();\" value=\"Insert NoCount Result\" /><br/>\n");
    }
}
