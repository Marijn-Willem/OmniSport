package com.cyclingroad.servlet.html;

import com.sports.entity.Sport;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class EventTeamImport extends com.sportservlet.html.EventTeamImport {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPortal?spid=" + Sport.sportIdCyclingRoad + "&" + compSeasonUrlParameters;
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
