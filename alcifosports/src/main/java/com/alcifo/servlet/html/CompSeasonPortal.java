package com.alcifo.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class CompSeasonPortal extends com.sportservlet.html.CompSeasonPortal {
    @Override
    protected int getSportId(HttpServletRequest req) {
        return getIntValuedParameterValue(req, "spid");
    }

    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "SportList";
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
