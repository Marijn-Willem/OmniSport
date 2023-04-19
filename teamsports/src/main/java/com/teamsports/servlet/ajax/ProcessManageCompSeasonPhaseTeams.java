package com.teamsports.servlet.ajax;

import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProcessManageCompSeasonPhaseTeams extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String[] tids = req.getParameterValues("tid");

        if (tids != null) {
            int pid = getIntValuedParameterValue(req, "pid");
            CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, pid);

            List<CompSeasonPhaseTeamKey> keys = new ArrayList<>();

            for (String tid : tids)
                keys.add(new CompSeasonPhaseTeamKey(cspk, Integer.parseInt(tid)));

            new CompSeasonPhaseTeamManager(stat).insertPhaseParticipantKeyList(keys);

            resp.getWriter().append("Teams successfully added to phase!");
        }
    }
}
