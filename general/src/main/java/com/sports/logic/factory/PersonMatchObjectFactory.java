package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.PersonMatchManager;
import com.sports.logic.calculation.PersonStandingProcessor;
import com.sports.logic.calculation.StandingProcessor;

import java.sql.Statement;

public class PersonMatchObjectFactory extends H2HObjectFactory<CompSeasonPersonSportKey,
        CompSeasonPhasePersonSportKey,
        PersonSport,
        SuperKeyEntity,
        PersonMatchKey,
        PersonMatch,
        PersonMatchPartKey,
        PersonMatchPart,
        PersonMatchPartStatKey,
        PersonMatchPartStat> {
    @Override
    public H2HMatchManager<PersonMatchKey, PersonMatch> getManager(Statement stat) {
        return new PersonMatchManager(stat);
    }

    @Override
    public PersonMatchKey getKey(CompSeasonKey compSeasonKey, int specifId) {
        return new PersonMatchKey(compSeasonKey, specifId);
    }

    @Override
    public PersonMatch getMatch() {
        return new PersonMatch();
    }

    @Override
    public StandingProcessor<CompSeasonPersonSportKey,
            CompSeasonPhasePersonSportKey,
            PersonSport,
            SuperKeyEntity,
            PersonMatchKey,
            PersonMatch,
            PersonMatchPartKey,
            PersonMatchPart,
            PersonMatchPartStatKey,
            PersonMatchPartStat> getStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        return new PersonStandingProcessor(stat, cspk);
    }

    @Override
    public PersonMatchPartObjectFactory getPartObjectFactory() {
        return new PersonMatchPartObjectFactory();
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManagePersonMatch";
    }
}
