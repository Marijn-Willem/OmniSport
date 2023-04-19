package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonEventPartRankingKey extends CacheDataKey {
    private final int competitiondId;
    private final int seasondId;
    private final int sportEventId;
    private final int compSeasonEventPartId;
    private final int clientId;

    public CompSeasonEventPartRankingKey(int competitiondId, int seasondId, int sportEventId,
                                         int compSeasonEventPartId, int clientId) {
        this.competitiondId = competitiondId;
        this.seasondId = seasondId;
        this.sportEventId = sportEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitiondId), Integer.toString(seasondId), Integer.toString(sportEventId),
                Integer.toString(compSeasonEventPartId), Integer.toString(clientId)
        }, "|");
    }
}
