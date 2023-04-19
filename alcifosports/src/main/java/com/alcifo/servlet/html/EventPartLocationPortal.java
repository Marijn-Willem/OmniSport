package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class EventPartLocationPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsList.add("eventpartlocation");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initPortal();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeVarInScriptTag("eid", getIntValuedParameterValue(req, "eid"), w);
        writeVarInScriptTag("csepid", getIntValuedParameterValue(req, "csepid"), w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters + "&eid=" + eid;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<div><select id=\"eplid\">\n</select></div>\n");
        w.append("<input type=\"button\" onclick=\"goToManageEventPartLocation();\" value=\"Manage location in event part\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToInsertEventPartLocation();\" value=\"Insert location in event part\" /><br/>\n");
    }
}
