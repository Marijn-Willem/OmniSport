package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventDisciplinePartRankingKey extends CacheDataKey {
    private final int competitiondId;
    private final int seasondId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int eventDisciplinePartId;
    private final int clientId;

    public EventDisciplinePartRankingKey(int competitiondId, int seasondId, int compSeasonEventId, int compSeasonEventPartId,
                                         int eventDisciplinePartId, int clientId) {
        this.competitiondId = competitiondId;
        this.seasondId = seasondId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.eventDisciplinePartId = eventDisciplinePartId;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitiondId), Integer.toString(seasondId),
                Integer.toString(compSeasonEventId), Integer.toString(compSeasonEventPartId),
                Integer.toString(eventDisciplinePartId), Integer.toString(clientId)
        }, "|");
    }
}
