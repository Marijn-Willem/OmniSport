package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageCompSeasonEvent extends com.sportservlet.html.ManageCompSeasonEvent {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPortal?" + compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
