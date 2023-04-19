package com.sports.logic.calculation;

import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.CompSeasonPersonSportFactory;

import java.sql.Statement;

public class PersonStandingProcessor extends StandingProcessor<PersonSport> {
    public PersonStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        super(stat, cspk);
    }

    protected CompSeasonParticipantFactory getFactory() {
        return new CompSeasonPersonSportFactory();
    }
}
