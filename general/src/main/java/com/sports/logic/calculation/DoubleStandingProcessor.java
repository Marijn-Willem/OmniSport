package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.Double;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonDoubleFactory;

import java.sql.Statement;

public class DoubleStandingProcessor extends StandingProcessor<CompSeasonDoubleKey,
        CompSeasonPhaseDoubleKey,
        Double,
        SuperKeyEntity,
        SuperKeyEntity,
        DoublesMatchKey,
        DoublesMatch,
        DoublesMatchPartKey,
        DoublesMatchPart,
        DoublesMatchPartStatKey,
        DoublesMatchPartStat> {
    public DoubleStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) {
        super(stat, cspk);
    }

    protected CompSeasonDoubleFactory getFactory() {
        return new CompSeasonDoubleFactory();
    }
}
