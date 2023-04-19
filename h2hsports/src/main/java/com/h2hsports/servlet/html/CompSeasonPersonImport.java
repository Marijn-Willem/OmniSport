package com.h2hsports.servlet.html;

import com.sports.logic.calculation.DbCalculation;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPersonImport extends com.sportservlet.html.CompSeasonPersonImport {
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPortal?spid=" + new DbCalculation(stat).getSportId(competitionId) + "&" +
                compSeasonUrlParameters;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
