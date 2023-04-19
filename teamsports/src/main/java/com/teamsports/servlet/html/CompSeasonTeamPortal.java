package com.teamsports.servlet.html;

import com.sports.entity.Competition;
import com.sports.entity.manager.CompetitionManager;
import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonTeamPortal extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseasonteam");
    }

    @Override
    protected void writeBodyTag(Writer w) throws IOException {
        w.append("<body onload=\"loadCompSeasonTeams();\">\n");
    }

    @Override
    protected void processScriptTag(Statement stat, HttpServletRequest req, Writer w) throws IOException {
        writeCompSeasonVarsInScriptTag(w);
        writeInitStateVarInScriptTag("tid", req, w);
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

        Writer w = res.getWriter();

        w.append("<select id=\"selTid\"></select><br/>\n");
        w.append("<input type=\"button\" onclick=\"goToManageCompSeasonTeam();\" value=\"Manage Team in Competition Season\" /><br/>\n");
        if (competition.isDomestic())
            writeLink("ManageCompSeasonTeams?" + compSeasonUrlParameters, "Manage teams in competition season", w);
    }
}
