package com.sports.calc.teamsports;

import com.sports.entity.TeamMatch;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.TeamManager;
import com.sports.entity.manager.TeamMatchManager;
import com.sports.logic.calculation.TeamStandingProcessor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DivisionStandingProcessor extends TeamStandingProcessor {
    private final CompDivisionKey cdk;

    DivisionStandingProcessor(Statement stat, CompSeasonPhaseKey cspk, int sportId, CompDivisionKey cdk) {
        super(stat, cspk, sportId);
        this.cdk = cdk;
    }

    @Override
    protected void setParticipantsAndMatches() throws SQLException {
        super.setParticipantsAndMatches();

        ParticipantManager tm = new TeamManager(stat);
        TeamMatchManager tmm = new TeamMatchManager(stat);

        List<Integer> teamIds = new DbCalculation(stat).getTeamsIdsInCompDivision(cspk.getSuperKey(), cdk);
        List<CompSeasonPhaseTeamKey> phaseTeamKeys = new ArrayList<>();

        for (int teamId : teamIds)
            phaseTeamKeys.add(new CompSeasonPhaseTeamKey(cspk, teamId));

        particMap = tm.getParticipantMap(teamIds);

        List<TeamMatch> teamMatches = tmm.getPlayedMatchesCompSeasonPhaseTeam(phaseTeamKeys);
        h2HMatches = new ArrayList<>();
        h2HMatches.addAll(teamMatches);
    }
}
