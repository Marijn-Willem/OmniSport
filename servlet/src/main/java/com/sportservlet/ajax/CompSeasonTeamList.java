package com.sportservlet.ajax;

import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.TeamManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonTeamList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Integer> teamIds = new CompSeasonTeamManager(stat).getParticipantIdsCompSeason(compSeasonKey);
        List<Team> teams = new TeamManager(stat).getTeamList(teamIds);
        teams.sort(new DescribedEntityDescription());

        for (Team team : teams)
            ServletUtil.writeOption(team.getId(), team.getDescription(), resp.getWriter());
    }
}
