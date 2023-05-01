package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageEventDisciplinePart extends com.sportservlet.html.ManageEventDisciplinePart {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "EventDisciplinePartPortal?" + compSeasonUrlParameters + "&cseid=" +
                getIntValuedParameterValue(req, "cseid") + "&csepid=" +
                getIntValuedParameterValue(req, "csepid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
