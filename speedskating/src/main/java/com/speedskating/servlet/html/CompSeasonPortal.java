package com.speedskating.servlet.html;

import com.sports.entity.Sport;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class CompSeasonPortal extends com.sportservlet.html.CompSeasonPortal {
    protected int getSportId(HttpServletRequest req) {
        return Sport.sportIdSpeedSkating;
    }

    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "Welcome";
    }

    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("compseason");
        cssList.add("styling");
    }
}
