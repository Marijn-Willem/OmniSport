package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageEventPartTeams extends com.sportservlet.html.ManageEventPartTeams {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters +
                "&cseid=" + getIntValuedParameterValue(req, "cseid") +
                "&csepid=" + getIntValuedParameterValue(req, "csepid");
    }
}
