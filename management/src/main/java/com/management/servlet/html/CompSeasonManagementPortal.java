package com.management.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompSeasonManagementPortal extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseason");
        jsList.add("compseason");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initCompSeasonManagementPortal();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("cid", competitionId, w);
        writeInitStateVarInScriptTag("sid", req, w);
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompetitionPortal?cid=" + competitionId;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"selSid\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageAddCompSeason();\" value=\"Add Competition Season\" /><br/>");
        w.append("<input type=\"button\" onclick=\"goToManageCompSeason();\" value=\"Manage Competition Season\" /><br/>");
        w.append("<input type=\"button\" onclick=\"goToManageClientCompSeasons();\" value=\"Manage Clients\" /><br/>");
    }
}
