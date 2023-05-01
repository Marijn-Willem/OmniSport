package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class EventPartNamePortal extends SuperHtmlServlet {
    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initPortal();\">\n");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("eventpartname");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<select id=\"epnid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageEventPartName();\" value=\"Manage Event Part Name\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToInsertEventPartName();\" value=\"Insert Event Part Name\" /><br/>\n");
    }
}
