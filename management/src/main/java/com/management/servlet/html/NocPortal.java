package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class NocPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("noc");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initNocList();\">\n");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EntityPortal";
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"nid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageNoc();\" value=\"Manage Noc\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddNoc();\" value=\"Add Noc\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToEntityInstancePortal();\" value=\"Manage instances\" /><br/>\n");
    }
}
