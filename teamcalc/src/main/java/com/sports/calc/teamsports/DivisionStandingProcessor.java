package com.sports.calc.teamsports;

import com.sports.entity.Team;
import com.sports.entity.TeamMatch;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.TeamManager;
import com.sports.logic.calculation.TeamStandingProcessor;

import java.sql.SQLException;
import java.sql.Statement;
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

        ParticipantManager<Team> tm = new TeamManager(stat);

        List<Integer> teamIds = new DbCalculation(stat).getTeamsIdsInCompDivision(cspk.getSuperKey(), cdk);

        particMap = tm.getParticipantMap(teamIds);

        List<TeamMatch> relevantMatches = h2HMatches.stream().filter(x ->
                particMap.containsKey(x.getTeamHomeId()) || particMap.containsKey(x.getTeamAwayId())).toList();
        h2HMatches.clear();
        h2HMatches.addAll(relevantMatches);
    }
}
