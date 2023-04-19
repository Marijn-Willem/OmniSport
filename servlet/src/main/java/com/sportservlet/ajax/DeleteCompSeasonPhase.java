package com.sportservlet.ajax;

import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteCompSeasonPhase extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int compId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        int phaseId = Integer.parseInt(req.getParameter("pid"));

        new CompSeasonPhaseManager(stat).delete(new CompSeasonPhaseKey(new CompSeasonKey(compId, seasonId), phaseId));
    }
}
