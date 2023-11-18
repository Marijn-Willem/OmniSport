package com.h2hsports.servlet.html;

import com.sports.entity.manager.CompetitionManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageH2HMatch extends com.sportservlet.html.ManageH2HMatch {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        String returnPath;

        if ("i".equals(mode)) {
            int spid = new CompetitionManager(stat).getCompetition(competitionId).getSportId();
            returnPath = "CompSeasonPortal?spid=" + spid + "&" + compSeasonUrlParameters;
        }
        else
            returnPath = "MatchOverview?" + compSeasonUrlParameters + "&pid=" + req.getParameter("pid");

        return returnPath;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
