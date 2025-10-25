package com.h2hsports.servlet.ajax;

import com.sports.entity.Competition;
import com.sports.entity.manager.CompetitionManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPorts extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

        Writer w = resp.getWriter();

        if (competition.isH2hDouble())
            ServletUtil.writeGenericGoToButton("CompSeasonDoublesImport", compSeasonUrlParameters, "Import doubles", w);
        else
            ServletUtil.writeGenericGoToButton("CompSeasonPersonImport", compSeasonUrlParameters, "Import persons", w);

        ServletUtil.writeGenericGoToButton("KnockoutMain", compSeasonUrlParameters, "Manage knockout", w);
        ServletUtil.writeGenericGoToButton("MatchOverview", compSeasonUrlParameters, "Match overview", w);
        ServletUtil.writeGenericGoToButton("CopyCompSeason", compSeasonUrlParameters, "Copy Competition Season", w);
        ServletUtil.writeGenericGoToButton("CompSeasonPhaseMain", compSeasonUrlParameters, "Manage Phases", w);
    }
}
