package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class ManageCompSeasonPhase extends com.sportservlet.html.ManageCompSeasonPhase {
    public String getBasicReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPhasePortal?cid=" + competitionId + "&sid=" + seasonId;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
