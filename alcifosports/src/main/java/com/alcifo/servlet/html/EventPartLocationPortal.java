package com.alcifo.servlet.html;

import com.sports.logic.util.Util;
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
        writeCompSeasonEventPartVarsInScriptTag(req, w);
        writeInitStateVarInScriptTag("eplid", req, w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        int cseid = getIntValuedParameterValue(req, "cseid");
        int csepid = getIntValuedParameterValue(req, "csepid");
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters + "&cseid=" + cseid + "&csepid=" + csepid;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<div><select id=\"eplid\">\n</select></div>\n");
        w.append("<input type=\"button\" onclick=\"goToManageEventPartLocation();\" value=\"Manage location in event part\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToInsertEventPartLocation();\" value=\"Insert location in event part\" /><br/>\n");
    }
}
