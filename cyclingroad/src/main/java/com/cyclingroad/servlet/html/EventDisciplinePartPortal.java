package com.cyclingroad.servlet.html;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.Statement;

public class EventDisciplinePartPortal extends com.sportservlet.html.EventDisciplinePartPortal {
    @Override
    public String getReturnPath(Statement stat, HttpServletRequest req) {
        return "CompSeasonEventPartPortal?" + compSeasonUrlParameters + "&eid=" +
                getIntValuedParameterValue(req, "eid");
    }

    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        jsSpecificList.add("eventdisciplinepart");
        cssList.add("styling");
    }
}
