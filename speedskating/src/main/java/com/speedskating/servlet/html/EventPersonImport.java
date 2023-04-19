package com.speedskating.servlet.html;

import com.sports.entity.Sport;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class EventPersonImport extends com.sportservlet.html.EventPersonImport {
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }

    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonPortal?spid=" + Sport.sportIdSpeedSkating + "&" + compSeasonUrlParameters;
    }
}
