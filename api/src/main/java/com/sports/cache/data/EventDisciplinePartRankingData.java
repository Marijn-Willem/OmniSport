package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EventDisciplinePartRankingKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.key.SuperKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.AlcifoParticipantFactory;

public class EventDisciplinePartRankingData extends AlcifoPartRankingData {
    private final int eventDisciplinePartId;

    public EventDisciplinePartRankingData(int competitionId, int seasonId, int sportEventId, int compSeasonEventPartId,
                                          int eventDisciplinePartId, Integer clientId) {
        super(competitionId, seasonId, sportEventId, compSeasonEventPartId, clientId);
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new EventDisciplinePartRankingKey(competitionId, seasonId, sportEventId, compSeasonEventPartId,
                eventDisciplinePartId, clientId);
    }

    @Override
    AlcifoPartParticipantFactory getFactory(AlcifoParticipantFactory participantFactory) {
        return participantFactory.getDisciplinePartParticipantFactory();
    }

    @Override
    SuperKey getKey(CompSeasonEventPartKey compSeasonEventPartKey) {
        return new EventDisciplinePartKey(compSeasonEventPartKey, eventDisciplinePartId);
    }
}
