package com.sports.logic.calculation;

import com.sports.entity.Double;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.logic.factory.CompSeasonDoubleFactory;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.Statement;

public class DoubleStandingProcessor extends StandingProcessor<Double> {
    public DoubleStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        super(stat, cspk);
    }

    protected CompSeasonParticipantFactory getFactory() {
        return new CompSeasonDoubleFactory();
    }
}
