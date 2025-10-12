package com.speedskating.servlet.ajax;

import com.sports.calc.speedskating.DbCalculation;
import com.sports.entity.key.CompSeasonEventKey;
import com.sportservlet.SuperServlet;
import com.sportservlet.flush.EventPersonSportRankFlusher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessCalculateEventPersonSportRanks extends SuperServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        int cseid = getIntValuedParameterValue(req, "cseid");

        CompSeasonEventKey compSeasonEventKey = new CompSeasonEventKey(compSeasonKey, cseid);
        new DbCalculation(stat).setEventPersonSportRanks(compSeasonEventKey);

        cacheFlusher = new EventPersonSportRankFlusher(compSeasonEventKey);

        resp.getWriter().append("EventPersonSport Ranks calculated successfully");
    }
}
