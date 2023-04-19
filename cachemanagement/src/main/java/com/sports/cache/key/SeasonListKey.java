package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SeasonListKey extends CacheDataKey {
    private final int competitionId;
    private final int clientId;

    public SeasonListKey(int competitionId, int clientId) {
        this.competitionId = competitionId;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStringsWithDelimiter(Integer.toString(competitionId), Integer.toString(clientId), "|");
    }
}
