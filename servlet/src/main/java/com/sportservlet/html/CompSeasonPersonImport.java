package com.sportservlet.html;

import jakarta.servlet.http.HttpServletRequest;

public abstract class CompSeasonPersonImport extends SuperEntityImport {
    protected String getOnClick(HttpServletRequest req) {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));

        return "processImport(" + competitionId + ", " + seasonId + ");";
    }

    protected void initProperties(HttpServletRequest req) {
        jsList.add("general");
        jsList.add("compseasonperson");
        initSpecificProperties(req);
    }
}
