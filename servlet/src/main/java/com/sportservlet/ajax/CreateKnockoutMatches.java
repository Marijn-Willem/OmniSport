package com.sportservlet.ajax;

import com.sports.calc.h2hsports.DbCalculation;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateKnockoutMatches extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int compSeasonPhaseId = Integer.parseInt(req.getParameter("pid"));

        CompSeasonPhaseKey cspk =
                new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), compSeasonPhaseId);

        new DbCalculation(stat).createKnockoutMatches(
                new com.sports.logic.calculation.DbCalculation(stat)
                        .getCompSeasonParticipantFactory(competitionId).getH2HObjectFactory(), cspk);

        resp.getWriter().append("Successful!");
    }
}
