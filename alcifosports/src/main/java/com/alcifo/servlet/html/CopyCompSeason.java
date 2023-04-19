package com.alcifo.servlet.html;

import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class CopyCompSeason extends com.sportservlet.html.CopyCompSeason {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
