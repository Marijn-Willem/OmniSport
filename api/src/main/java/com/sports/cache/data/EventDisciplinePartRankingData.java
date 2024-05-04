package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.EventDisciplinePartRankingKey;
import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;
import com.sports.calc.alcifo.DisciplinePartTeamFactory;
import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.AlcifoParticipant;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.AlcifoParticipantFactory;

public class EventDisciplinePartRankingData extends AlcifoPartRankingData<EventDisciplinePartKey> {
    private final int eventDisciplinePartId;

    public EventDisciplinePartRankingData(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId,
                                          int eventDisciplinePartId, Integer clientId) {
        super(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, clientId);
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new EventDisciplinePartRankingKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId,
                eventDisciplinePartId, clientId);
    }

    @Override
    AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
        ? extends SuperKeyEntity,
        ? extends Participant,
        ? extends AlcifoParticipantKey,
        ? extends AlcifoParticipant,
        ? extends SuperKey,
        EventDisciplinePartKey,
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
            case PERSON_SPORT -> new DisciplinePartPersonSportFactory();
            case TEAM -> new DisciplinePartTeamFactory();
            default -> null;
        };
    }

    @Override
    EventDisciplinePartKey getKey(CompSeasonEventPartKey compSeasonEventPartKey) {
        return new EventDisciplinePartKey(compSeasonEventPartKey, eventDisciplinePartId);
    }
}
