package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageCompSeasonEventPart extends com.sportservlet.html.ManageCompSeasonEventPart {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPartPortal?cid=" + competitionId + "&sid=" + seasonId + "&eid=" +
                getIntValuedParameterValue(req, "eid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
