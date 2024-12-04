package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.Statement;

public class CompSeasonTeamFactory extends CompSeasonParticipantFactory<CompSeasonTeamKey, CompSeasonPhaseTeamKey, Team, CompSeasonTeam, CompSeasonPhaseTeam, TeamMatchKey, TeamMatch, TeamMatchPartKey, TeamMatchPart, H2HMatchPartStatKey, H2HMatchPartStat> {
    @Override
    public TeamMatchObjectFactory getH2HObjectFactory() {
        return new TeamMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonTeamKey, CompSeasonTeam> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonTeamManager(stat);
    }

    @Override
    public ParticipantManager<Team> getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<CompSeasonTeamKey, CompSeasonPhaseTeamKey, CompSeasonPhaseTeam> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseTeamManager(stat);
    }

    @Override
    public CompSeasonTeamKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonTeamKey(csk, specifId);
    }

    @Override
    public CompSeasonPhaseTeamKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
        return new CompSeasonPhaseTeamKey(cspk, specifId);
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.TEAM;
    }

    @Override
    public String getParticipantDescription() {
        return "Team";
    }

    @Override
    public Team getCopyForStanding(Team participant) {
        Team team = new Team();
        participant.copyToForStanding(team);

        return team;
    }
}
