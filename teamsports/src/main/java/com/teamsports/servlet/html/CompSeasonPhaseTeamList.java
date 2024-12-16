package com.teamsports.servlet.html;

import com.sports.entity.Team;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompSeasonPhaseTeamManager;
import com.sports.entity.manager.TeamManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class CompSeasonPhaseTeamList extends SuperHtmlServlet {
    @Override
    void initSpecificProperties(HttpServletRequest req) { }

    @Override
    protected String getReturnPath(Statement stat, HttpServletRequest req) throws SQLException {
        return "CompSeasonPhasePortal?" + compSeasonUrlParameters + "&pid=" +
                getIntValuedParameterValue(req, "pid");
    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        int pid = getIntValuedParameterValue(req, "pid");

        CompSeasonPhaseKey cspk = new CompSeasonPhaseKey(compSeasonKey, pid);

        List<Integer> teamIds = new CompSeasonPhaseTeamManager(stat).getTeamsInCompSeasonPhases(
                Collections.singletonList(cspk));

        List<Team> teamList = new TeamManager(stat).getTeamList(teamIds);
        teamList.sort(new DescribedEntityDescription());

        Writer w = res.getWriter();

        for (Team team : teamList) {
            String href = "CompSeasonPhaseTeamCorrectionPortal?" + compSeasonUrlParameters + "&pid=" + pid + "&tid=" + team.getId();
            writeLink(href, team.getDescription(), w);
        }
    }
}
