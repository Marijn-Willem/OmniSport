package com.darts.servlet.html;

import com.sports.entity.Sport;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class MatchOverview extends com.sportservlet.html.MatchOverview {
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPortal?spid=" + Sport.sportIdDarts + "&" + compSeasonUrlParameters;
    }

    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    protected String getAddMatchLink() {
        return null;
    }
}
