package com.cyclingroad.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManageDisciplinePartPersonSports extends com.sportservlet.html.ManageDisciplinePartPersonSports {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EventDisciplinePartPortal?" + compSeasonUrlParameters +
                "&eid=" + getIntValuedParameterValue(req, "eid") +
                "&csepid=" + getIntValuedParameterValue(req, "csepid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
