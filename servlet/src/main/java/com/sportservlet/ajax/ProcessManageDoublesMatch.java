package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonDoubleFactory;
import com.sports.logic.factory.CompSeasonParticipantFactory;

public class ProcessManageDoublesMatch extends ProcessManageH2HMatch<DoublesMatchKey, DoublesMatch> {
    @Override
    protected CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            DoublesMatchKey,
            DoublesMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getFactory() {
        return new CompSeasonDoubleFactory();
    }
}
