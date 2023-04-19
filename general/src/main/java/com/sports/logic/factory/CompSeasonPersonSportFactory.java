package com.sports.logic.factory;

import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.calculation.DbCalculation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CompSeasonPersonSportFactory implements CompSeasonParticipantFactory<CompSeasonPersonSportKey, SuperKeyEntity> {
    @Override
    public H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> getH2HObjectFactory() {
        return new PersonMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonPersonSportKey, SuperKeyEntity> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonPersonSportManager(stat);
    }

    @Override
    public ParticipantManager<? extends Participant> getParticipantManager(Statement stat) {
        return new PersonSportManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey, ? extends CompSeasonPhaseParticipantKey, ? extends SuperKeyEntity> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhasePersonSportManager(stat);
    }

    @Override
    public CompSeasonParticipantKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonPersonSportKey(csk, specifId);
    }

    @Override
    public CompSeasonPhaseParticipantKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
        return new CompSeasonPhasePersonSportKey(cspk, specifId);
    }

    @Override
    public Map<String, ? extends Participant> getDescriptionParticipantMap(Statement stat, List<String> descriptions, int competitionId) throws SQLException {
        return new DbCalculation(stat).getDescrPersonSportMapWithNewInstances(descriptions, competitionId);
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.PERSON_SPORT;
    }

    @Override
    public String getParticipantDescription() {
        return "Person";
    }
}
