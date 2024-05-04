package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.Double;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.Statement;

public class CompSeasonDoubleFactory extends CompSeasonParticipantFactory<CompSeasonDoubleKey, CompSeasonPhaseDoubleKey, Double, SuperKeyEntity, SuperKeyEntity, DoublesMatchKey, DoublesMatch, DoublesMatchPartKey, DoublesMatchPart, DoublesMatchPartStatKey, DoublesMatchPartStat> {
    @Override
    public DoublesMatchObjectFactory getH2HObjectFactory() {
        return new DoublesMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonDoubleKey, SuperKeyEntity> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonDoubleManager(stat);
    }

    @Override
    public ParticipantManager<Double> getParticipantManager(Statement stat) {
        return new DoubleManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<CompSeasonDoubleKey, CompSeasonPhaseDoubleKey, SuperKeyEntity> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseDoubleManager(stat);
    }

    @Override
    public CompSeasonDoubleKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonDoubleKey(csk, specifId);
    }

    @Override
    public CompSeasonPhaseDoubleKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
        return new CompSeasonPhaseDoubleKey(cspk, specifId);
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.DOUBLE;
    }

    @Override
    public String getParticipantDescription() {
        return "Double";
    }
}
