package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.CompSeasonPersonSportFactory;

public class ProcessManagePersonMatch extends ProcessManageH2HMatch<PersonMatchKey, PersonMatch> {
    @Override
    protected CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            PersonMatchKey,
            PersonMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getFactory() {
        return new CompSeasonPersonSportFactory();
    }
}
