package com.sportservlet.ajax;

import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ProcessCopyCompSeason extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int seasonIdTo = Integer.parseInt(req.getParameter("sidt"));

        CompSeasonKey csk = new CompSeasonKey(competitionId, seasonId);

        if (new DbCalculation(stat).copyCompSeasonToSeason(csk, seasonIdTo))
            resp.getWriter().append("Copy successful");
        else
            resp.getWriter().append("Copy not successful");
    }
}
