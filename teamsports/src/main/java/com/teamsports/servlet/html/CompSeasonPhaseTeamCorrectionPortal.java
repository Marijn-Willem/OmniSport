package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompSeasonPhaseTeamCorrectionPortal extends SuperHtmlServlet {
    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonPhaseVarsInScriptTag(req, w);
        writeVarInScriptTag("tid", getIntValuedParameterValue(req, "tid"), w);
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadCorrections();\">\n");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        int pid = getIntValuedParameterValue(req, "pid");

        return "CompSeasonPhaseTeamList?" + compSeasonUrlParameters + "&pid=" + pid;
    }

    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonphaseteamcorrection");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Writer w = res.getWriter();

        w.append("<select id=\"csptcid\">\n</select>\n");
        w.append("<br/><input type=\"button\" onclick=\"goToManageCompSeasonPhaseTeamCorrection();\" value=\"Manage Correction\" />\n");
        w.append("<br/><input type=\"button\" onclick=\"goToInsertCompSeasonPhaseTeamCorrection();\" value=\"Insert Correction\" />\n");
    }
}
