package com.speedskating.servlet.ajax;

import com.sports.calc.speedskating.DbCalculation;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TotalRanking extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int eventId = Integer.parseInt(req.getParameter("eid"));
        int eventPartId = Integer.parseInt(req.getParameter("epid"));

        CompSeasonEventPartKey csepk =
                new CompSeasonEventPartKey(
                        new CompSeasonEventKey(
                                new CompSeasonKey(competitionId, seasonId), eventId),
                        eventPartId);

        List<EventPartPersonSport> ranking = new DbCalculation(stat).getTotalRanking(csepk);

        int rnk = 0, nr = 0;
        double prevPoints = 0.0;

        Writer w = resp.getWriter();

        for (EventPartPersonSport eventPartPersonSport : ranking) {
            nr++;
            double points = eventPartPersonSport.getResultPoints();

            if (points != prevPoints)
                rnk = nr;

            String rule = "<tr><td>" + rnk + ".</td><td>" + eventPartPersonSport.getPersonSportDescription() + "</td>" +
                    "<td>" + Util.getDoubleAsStringWith3Digits(points) + "</td></tr>\n";

            w.append(rule);

            prevPoints = points;
        }
    }
}
