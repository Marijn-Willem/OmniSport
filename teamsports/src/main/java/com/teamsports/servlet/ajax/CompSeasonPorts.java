package com.teamsports.servlet.ajax;

import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Statement;

public class CompSeasonPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer w = resp.getWriter();

        writeGoToButton("StandingByCompSeason", "Standing", w);
        writeGoToButton("MatchOverview", "Match Overview", w);
        writeGoToButton("CompSeasonPhasePortal", "Manage Phases", w);
        writeGoToButton("CompSeasonTeamPortal", "Manage teams in competition season", w);
        writeGoToButton("ManageCompSeasonDivisions", "Manage divisions in competition season", w);
        writeGoToButton("CompSeasonTeamImport", "Import Teams", w);
        writeGoToButton("TeamMatchActionImport", "Import match actions", w);
    }

    protected void writeGoToButton(String url, String text, Writer w) throws IOException {
        ServletUtil.writeGenericGoToButton(url, compSeasonUrlParameters, text, w);
    }
}
