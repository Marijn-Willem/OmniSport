package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class PhaseTypePortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("phasetype");
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
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"ptid\">\n</select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManagePhaseType();\" value=\"Manage Phase type\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToInsertPhaseType();\" value=\"Insert Phase type\" /><br/>\n");
    }
}
