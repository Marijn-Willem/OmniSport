package com.teamsports.servlet.ajax;

import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sports.entity.manager.TeamManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class TeamListCompSeasonPhase extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int competitionId = Integer.parseInt(req.getParameter("cid"));
        int seasonId = Integer.parseInt(req.getParameter("sid"));
        String[] phaseArr = req.getParameter("pid").split("_");
        int phaseId = Integer.parseInt(phaseArr[0]);

        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), phaseId);

        TeamManager tm = new TeamManager(stat);
        CompSeasonPhaseTeamManager cscpm = new CompSeasonPhaseTeamManager(stat);

        List<Team> teamList = tm.getTeamList(cscpm.getTeamsInCompSeasonPhases(Collections.singletonList(cspk)));

        teamList.sort(new DescribedEntityDescription());

        Writer w = resp.getWriter();

        for (Team team : teamList) {
            String rule = "<option value=\"" + team.getId() + "\">" + team.getDescription() + "</option>\n";
            w.append(rule);
        }
    }
}
