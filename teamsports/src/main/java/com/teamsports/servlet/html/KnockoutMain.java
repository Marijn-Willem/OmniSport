package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class KnockoutMain extends com.sportservlet.html.KnockoutMain {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPhasePortal?" + compSeasonUrlParameters + "&pid=" +
                getIntValuedParameterValue(req, "pid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
