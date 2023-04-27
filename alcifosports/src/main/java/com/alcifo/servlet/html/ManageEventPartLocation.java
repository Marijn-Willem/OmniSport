package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageEventPartLocation extends com.sportservlet.html.ManageEventPartLocation {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int cseid = getIntValuedParameterValue(req, "cseid");
        int csepid = getIntValuedParameterValue(req, "csepid");

        return "EventPartLocationPortal?" + compSeasonUrlParameters + "&cseid=" + cseid + "&csepid=" + csepid;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
