package com.cyclingroad.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManageEventPartTeams extends com.sportservlet.html.ManageEventPartTeams {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters + "&eid=" +
                getIntValuedParameterValue(req, "eid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
