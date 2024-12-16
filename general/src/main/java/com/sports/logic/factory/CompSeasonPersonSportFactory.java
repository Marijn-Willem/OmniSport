package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.Statement;

public class CompSeasonPersonSportFactory extends CompSeasonParticipantFactory<CompSeasonPersonSportKey, CompSeasonPhasePersonSportKey, PersonSport, SuperKeyEntity, PersonMatchKey, PersonMatch, PersonMatchPartKey, PersonMatchPart, PersonMatchPartStatKey, PersonMatchPartStat> {
    @Override
    public H2HObjectFactory<CompSeasonPersonSportKey,
            CompSeasonPhasePersonSportKey,
            PersonSport,
            SuperKeyEntity,
            PersonMatchKey,
            PersonMatch,
            PersonMatchPartKey,
            PersonMatchPart,
            PersonMatchPartStatKey,
            PersonMatchPartStat> getH2HObjectFactory() {
        return new PersonMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonPersonSportKey, SuperKeyEntity> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonPersonSportManager(stat);
    }

    @Override
    public ParticipantManager<PersonSport> getParticipantManager(Statement stat) {
        return new PersonSportManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<CompSeasonPersonSportKey, CompSeasonPhasePersonSportKey> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhasePersonSportManager(stat);
    }

    @Override
    public CompSeasonPersonSportKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonPersonSportKey(csk, specifId);
    }

    @Override
    public CompSeasonPhasePersonSportKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
        return new CompSeasonPhasePersonSportKey(cspk, specifId);
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.PERSON_SPORT;
    }

    @Override
    public String getParticipantDescription() {
        return "Person";
    }

    @Override
    public PersonSport getCopyForStanding(PersonSport participant) {
        PersonSport personSport = new PersonSport();
        participant.copyToForStanding(personSport);

        return personSport;
    }
}
