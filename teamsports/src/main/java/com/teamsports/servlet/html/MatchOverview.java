package com.teamsports.servlet.html;

import com.sports.entity.manager.CompetitionManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class MatchOverview extends com.sportservlet.html.MatchOverview {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new CompetitionManager(stat).getCompetition(competitionId).getSportId() + "&" +
                compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected String getAddMatchLink() {
        return "ManageH2HMatch";
    }

    @Override
    protected String getParametersAddMatchLink() {
        return "md=i";
    }
}
