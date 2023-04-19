package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CompSeasonTeamFactory implements CompSeasonParticipantFactory<CompSeasonTeamKey, CompSeasonTeam> {
    @Override
    public H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> getH2HObjectFactory() {
        return new TeamMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonTeamKey, CompSeasonTeam> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonTeamManager(stat);
    }

    @Override
    public ParticipantManager<? extends Participant> getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey, ? extends CompSeasonPhaseParticipantKey, ? extends SuperKeyEntity> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseTeamManager(stat);
    }

    @Override
    public CompSeasonParticipantKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonTeamKey(csk, specifId);
    }

    @Override
    public CompSeasonPhaseParticipantKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
        return new CompSeasonPhaseTeamKey(cspk, specifId);
    }

    @Override
    public Map<String, ? extends Participant> getDescriptionParticipantMap(Statement stat, List<String> descriptions, int competitionId) throws SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

        return new TeamManager(stat).getDescrTeamMapBySportGender(descriptions, competition.getSportId(), competition.getGenderId());
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.TEAM;
    }

    @Override
    public String getParticipantDescription() {
        return "Team";
    }
}
