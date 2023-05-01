package com.cyclingroad.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManageDisciplinePartTeams extends com.sportservlet.html.ManageDisciplinePartTeams {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "EventDisciplinePartPortal?" + compSeasonUrlParameters +
                "&cseid=" + getIntValuedParameterValue(req, "cseid") +
                "&csepid=" + getIntValuedParameterValue(req, "csepid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
