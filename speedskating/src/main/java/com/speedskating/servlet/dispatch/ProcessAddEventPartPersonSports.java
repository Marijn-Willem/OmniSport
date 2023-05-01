package com.speedskating.servlet.dispatch;

import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartPersonSportKey;
import com.sports.entity.manager.EventPartPersonSportManager;
import com.sportservlet.dispatch.SuperDispatchServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessAddEventPartPersonSports extends SuperDispatchServlet {
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int compSeasonEventId = Integer.parseInt(req.getParameter("cseid"));

        dispatchURL = "EventHeats?cid=" + competitionId + "&sid=" + seasonId + "&cseid=" + compSeasonEventId;

        String[] personSportIds = req.getParameterValues("pid");

        if (personSportIds != null) {
            CompSeasonEventPartKey csepk = getCompSeasonEventPartKey(req);

            EventPartPersonSportManager eppm = new EventPartPersonSportManager(stat);

            for (String psId : personSportIds) {
                EventPartPersonSportKey eventPartPersonSportKey = new EventPartPersonSportKey(csepk, Integer.parseInt(psId));
                eppm.insertEventPartPersonSport(eventPartPersonSportKey, new EventPartPersonSport());
            }
        }
    }
}
