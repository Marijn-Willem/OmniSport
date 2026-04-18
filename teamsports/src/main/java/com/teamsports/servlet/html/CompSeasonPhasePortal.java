package com.teamsports.servlet.html;

import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPhasePortal extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {
        jsList.add("compseasonphase");
        jsSpecificList.add("compseasonphase");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"initCompSeasonPhaseList();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeInitStateVarInScriptTag("pid", req, w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Writer w = res.getWriter();

        w.append("<select id=\"selPid\" onchange=\"handleChangePid();\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageCompSeasonPhase();\" value=\"Manage Phase\" /><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToAddCompSeasonPhase();\" value=\"Add Phase\" /><br/>\n");
        w.append("<input id=\"btnPt\" type=\"button\" onclick=\"goToManageCompSeasonPhaseTeams();\" value=\"Manage Teams in Phase\" /><br/>\n");
        w.append("<input id=\"btnKo\" type=\"button\" onclick=\"goToManageKnockout();\" value=\"Manage Knockout\" /><br/>\n");
    }
}
