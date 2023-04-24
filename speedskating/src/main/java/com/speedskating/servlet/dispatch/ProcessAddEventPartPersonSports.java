package com.speedskating.servlet.dispatch;

import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
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
        int eventId = Integer.parseInt(req.getParameter("eid"));

        dispatchURL = "EventHeats?cid=" + competitionId + "&sid=" + seasonId + "&eid=" + eventId;

        String[] personSportIds = req.getParameterValues("pid");

        if (personSportIds != null) {
            int eventPartId = Integer.parseInt(req.getParameter("epid"));

            CompSeasonEventPartKey csepk =
                    new CompSeasonEventPartKey(
                            new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), eventId),
                            eventPartId);

            EventPartPersonSportManager eppm = new EventPartPersonSportManager(stat);

            for (String psId : personSportIds) {
                EventPartPersonSportKey eventPartPersonSportKey = new EventPartPersonSportKey(csepk, Integer.parseInt(psId));
                eppm.insertEventPartPersonSport(eventPartPersonSportKey, new EventPartPersonSport());
            }
        }
    }
}
