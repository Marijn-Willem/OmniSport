package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class ManageCompSeasonPhase extends com.sportservlet.html.ManageCompSeasonPhase {
    @Override
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));

        return "CompSeasonPhaseMain?cid=" + competitionId + "&sid=" + seasonId;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
