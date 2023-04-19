package com.sportservlet.ajax;

import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.manager.TeamManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TeamListBySportGender extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        int gid = getIntValuedParameterValue(req, "gid");

        List<Team> teamList = new TeamManager(stat).getTeamListForSportGender(spid, gid);
        teamList.sort(new DescribedEntityDescription());

        for (Team team : teamList)
            ServletUtil.writeOption(team.getId(), team.getDescription(), resp.getWriter());
    }
}
