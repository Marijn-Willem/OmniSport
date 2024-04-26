package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.TeamMatchManager;
import com.sports.logic.calculation.RugbyStandingProcessor;
import com.sports.logic.calculation.StandingProcessor;
import com.sports.logic.calculation.TeamStandingProcessor;

import java.sql.SQLException;
import java.sql.Statement;

public class TeamMatchObjectFactory extends H2HObjectFactory<CompSeasonTeamKey,
        CompSeasonPhaseTeamKey,
        Team,
        CompSeasonTeam,
        CompSeasonPhaseTeam,
        TeamMatchKey,
        TeamMatch,
        TeamMatchPartKey,
        TeamMatchPart,
        H2HMatchPartStatKey,
        H2HMatchPartStat> {
    @Override
    public H2HMatchManager<TeamMatchKey, TeamMatch> getManager(Statement stat) {
        return new TeamMatchManager(stat);
    }

    @Override
    public TeamMatchKey getKey(CompSeasonKey compSeasonKey, int specifId) {
        return new TeamMatchKey(compSeasonKey, specifId);
    }

    @Override
    public TeamMatch getMatch() {
        return new TeamMatch();
    }

    @Override
    public StandingProcessor<CompSeasonTeamKey,
            CompSeasonPhaseTeamKey,
            Team,
            CompSeasonTeam,
            CompSeasonPhaseTeam,
            TeamMatchKey,
            TeamMatch,
            TeamMatchPartKey,
            TeamMatchPart,
            H2HMatchPartStatKey,
            H2HMatchPartStat> getStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) throws SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(cspk.getCompetitionId());

        if (competition.getSportId() == Sport.sportIdRugby) {
            CompSeason compSeason = new CompSeasonManager(stat).getCompSeason(cspk.getSuperKey());
            return new RugbyStandingProcessor(stat, cspk, compSeason);
        }

        return new TeamStandingProcessor(stat, cspk, competition.getSportId());
    }

    @Override
    public TeamMatchPartObjectFactory getPartObjectFactory() {
        return new TeamMatchPartObjectFactory();
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManageTeamMatch";
    }
}
