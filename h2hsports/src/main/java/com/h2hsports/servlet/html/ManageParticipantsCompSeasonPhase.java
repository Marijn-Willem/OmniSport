package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManageParticipantsCompSeasonPhase extends com.sportservlet.html.ManageParticipantsCompSeasonPhase {
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));

        return "KnockoutMain?cid=" + competitionId + "&sid=" + seasonId;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
