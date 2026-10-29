package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class EventTeamImport extends com.sportservlet.html.EventTeamImport {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPortal?" + compSeasonUrlParameters + "&cseid=" +
                getIntValuedParameterValue(req, "cseid");
    }
}
