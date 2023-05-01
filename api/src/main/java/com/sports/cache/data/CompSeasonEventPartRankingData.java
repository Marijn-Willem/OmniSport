package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonEventPartRankingKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.SuperKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.AlcifoParticipantFactory;

public class CompSeasonEventPartRankingData extends AlcifoPartRankingData {
    public CompSeasonEventPartRankingData(int competitionId, int seasonId, int compSeasonEventId,
                                          int compSeasonEventPartId, Integer clientId) {
        super(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId);
    }

    @Override
    AlcifoPartParticipantFactory getFactory(AlcifoParticipantFactory participantFactory) {
        return participantFactory.getEventPartParticipantFactory();
    }

    @Override
    SuperKey getKey(CompSeasonEventPartKey compSeasonEventPartKey) {
        return compSeasonEventPartKey;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new CompSeasonEventPartRankingKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId);
    }
}
