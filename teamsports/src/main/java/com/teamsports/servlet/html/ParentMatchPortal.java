package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ParentMatchPortal extends com.sportservlet.html.ParentMatchPortal {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "MatchOverview?" + compSeasonUrlParameters + "&pid=" + getIntValuedParameterValue(req, "pid");
    }
}
