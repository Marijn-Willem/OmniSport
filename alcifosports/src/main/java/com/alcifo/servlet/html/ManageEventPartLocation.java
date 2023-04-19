package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageEventPartLocation extends com.sportservlet.html.ManageEventPartLocation {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int eid = getIntValuedParameterValue(req, "eid");
        int csepid = getIntValuedParameterValue(req, "csepid");

        return "EventPartLocationPortal?" + compSeasonUrlParameters + "&eid=" + eid + "&csepid=" + csepid;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
