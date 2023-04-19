package com.sports.logic.calculation;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonDoubleKey;
import com.sports.logic.factory.CompSeasonDoubleFactory;

import java.sql.Statement;

class DoubleDedoubler extends CompSeasonParticipantDedoubler<CompSeasonDoubleKey, SuperKeyEntity> {
    CompSeasonDoubleFactory getFactory() {
        return new CompSeasonDoubleFactory();
    }

    DoubleDedoubler(Statement stat) {
        super(stat);
    }
}
