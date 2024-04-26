package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonPersonSportFactory;

import java.sql.Statement;

public class PersonStandingProcessor extends StandingProcessor<CompSeasonPersonSportKey,
        CompSeasonPhasePersonSportKey,
        PersonSport,
        SuperKeyEntity,
        SuperKeyEntity,
        PersonMatchKey,
        PersonMatch,
        PersonMatchPartKey,
        PersonMatchPart,
        PersonMatchPartStatKey,
        PersonMatchPartStat> {
    public PersonStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        super(stat, cspk);
    }

    protected CompSeasonPersonSportFactory getFactory() {
        return new CompSeasonPersonSportFactory();
    }
}
