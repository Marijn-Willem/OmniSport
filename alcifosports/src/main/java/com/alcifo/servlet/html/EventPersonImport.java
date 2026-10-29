package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class EventPersonImport extends com.sportservlet.html.EventPersonImport {
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
