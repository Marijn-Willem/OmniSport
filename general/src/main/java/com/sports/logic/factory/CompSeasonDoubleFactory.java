package com.sports.logic.factory;

import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.Statement;

public class CompSeasonDoubleFactory implements CompSeasonParticipantFactory<CompSeasonDoubleKey, SuperKeyEntity> {
    @Override
    public H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> getH2HObjectFactory() {
        return new DoublesMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonDoubleKey, SuperKeyEntity> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonDoubleManager(stat);
    }

    @Override
    public ParticipantManager<? extends Participant> getParticipantManager(Statement stat) {
        return new DoubleManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey, ? extends CompSeasonPhaseParticipantKey, ? extends SuperKeyEntity> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseDoubleManager(stat);
    }

    @Override
    public CompSeasonParticipantKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonDoubleKey(csk, specifId);
    }

    @Override
    public CompSeasonPhaseParticipantKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
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
