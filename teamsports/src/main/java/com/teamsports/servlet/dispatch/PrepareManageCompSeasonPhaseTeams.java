package com.teamsports.servlet.dispatch;

import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sportservlet.dispatch.SuperDispatchServlet;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class PrepareManageCompSeasonPhaseTeams extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        int pid = getIntValuedParameterValue(req, "pid");

        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, pid);

        List<Integer> teamIds = new CompSeasonPhaseTeamManager(stat).getTeamsInCompSeasonPhases(
                Collections.singletonList(cspk));

        dispatchURL = teamIds.size() > 0 ? "CompSeasonPhaseTeamList" : "ManageCompSeasonPhaseTeams";
    }
}
