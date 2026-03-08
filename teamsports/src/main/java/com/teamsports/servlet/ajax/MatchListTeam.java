package com.teamsports.servlet.ajax;

import com.sports.entity.CompSeasonPhase;
import com.sports.entity.Team;
import com.sports.entity.TeamMatch;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.TeamMatchManager;
import com.sports.logic.calculation.DbCalculation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MatchListTeam extends MatchList {
    private int teamId;

    @Override
    protected List<TeamMatch> getMatchList(TeamMatchManager mm, CompSeasonPhase csp) throws SQLException {
        CompSeasonPhaseTeamKey csptk = new CompSeasonPhaseTeamKey(csp.getCompSeasonPhaseKey(), teamId);

        return mm.getPlayedMatchesCompSeasonPhaseTeam(Collections.singletonList(csptk));
    }

    @Override
    protected void processTeamMap(Statement stat, Map<Integer, Team> teamMap) throws SQLException {
        List<Team> teamList = new ArrayList<>(teamMap.values());
        new DbCalculation(stat).addCompDivisionsToTeams(teamList, compSeasonKey);
    }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        teamId = Integer.parseInt(req.getParameter("tid"));
        super.processBody(stat, req, resp);
    }
}
