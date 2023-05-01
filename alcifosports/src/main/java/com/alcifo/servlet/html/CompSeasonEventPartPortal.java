package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class CompSeasonEventPartPortal extends com.sportservlet.html.CompSeasonEventPartPortal {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPortal?cid=" + competitionId + "&sid=" + seasonId + "&cseid=" +
                getIntValuedParameterValue(req, "cseid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
