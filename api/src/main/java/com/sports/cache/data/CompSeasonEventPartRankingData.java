package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonEventPartRankingKey;
import com.sports.calc.alcifo.EventPartPersonSportFactory;
import com.sports.calc.alcifo.EventPartTeamFactory;
import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.AlcifoParticipant;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.AlcifoParticipantFactory;

public class CompSeasonEventPartRankingData extends AlcifoPartRankingData<CompSeasonEventPartKey> {
    public CompSeasonEventPartRankingData(int competitionId, int seasonId, int compSeasonEventId,
                                          int compSeasonEventPartId, Integer clientId) {
        super(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId);
    }

    @Override
    AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
        ? extends SuperKeyEntity,
        ? extends Participant,
        ? extends AlcifoParticipantKey,
        ? extends AlcifoParticipant,
        ? extends SuperKey,
        CompSeasonEventPartKey,
        ? extends AlcifoPartParticipant,
        ? extends SuperKey,
        ? extends AlcifoPartParticipant> getFactory(AlcifoParticipantFactory<? extends CompSeasonParticipantKey,
                                            ? extends SuperKeyEntity,
                                            ? extends Participant,
                                            ? extends AlcifoParticipantKey,
                                            ? extends AlcifoParticipant,
                                            ? extends SuperKey,
                                            ? extends AlcifoPartParticipant> participantFactory) {
        return switch (participantFactory.getParticipantType()) {
            case PERSON_SPORT -> new EventPartPersonSportFactory();
            case TEAM -> new EventPartTeamFactory();
            default -> null;
        };
    }

    @Override
    CompSeasonEventPartKey getKey(CompSeasonEventPartKey compSeasonEventPartKey) {
        return compSeasonEventPartKey;
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new CompSeasonEventPartRankingKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId);
    }
}
