package com.teamsports.servlet.ajax;

import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProcessManageCompSeasonTeams extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String[] tids = req.getParameterValues("tid");

        if (tids != null) {
            List<CompSeasonTeamKey> compSeasonTeamKeyList = new ArrayList<>();

            for (String tid : tids)
                compSeasonTeamKeyList.add(new CompSeasonTeamKey(compSeasonKey, Integer.parseInt(tid)));

            new CompSeasonTeamManager(stat).insertCompSeasonParticipants(compSeasonTeamKeyList);

            resp.getWriter().append("Teams successfully added");
        }
    }
}
