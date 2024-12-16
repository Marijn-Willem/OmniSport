package com.sports.logic.calculation;

import com.sports.entity.DoublesMatch;
import com.sports.entity.DoublesMatchPart;
import com.sports.entity.DoublesMatchPartStat;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.Double;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonDoubleFactory;

import java.sql.Statement;

class DoubleDedoubler extends CompSeasonParticipantDedoubler<CompSeasonDoubleKey,
        CompSeasonPhaseDoubleKey,
        Double,
        SuperKeyEntity,
        DoublesMatchKey,
        DoublesMatch,
        DoublesMatchPartKey,
        DoublesMatchPart,
        DoublesMatchPartStatKey,
        DoublesMatchPartStat> {
    CompSeasonDoubleFactory getFactory() {
        return new CompSeasonDoubleFactory();
    }

    DoubleDedoubler(Statement stat) {
        super(stat);
    }
}
