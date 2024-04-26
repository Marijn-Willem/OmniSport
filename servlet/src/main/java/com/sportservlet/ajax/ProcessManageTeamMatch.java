package com.sportservlet.ajax;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import com.sports.logic.factory.CompSeasonTeamFactory;

public class ProcessManageTeamMatch extends ProcessManageH2HMatch<TeamMatchKey, TeamMatch> {
    @Override
    protected CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends SuperKeyEntity,
            TeamMatchKey,
            TeamMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> getFactory() {
        return new CompSeasonTeamFactory();
    }
}
