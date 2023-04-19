package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompetitionListKey extends CacheDataKey {
    private final int sportId;
    private final int clientId;

    public CompetitionListKey(int sportId, int clientId) {
        this.sportId = sportId;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStringsWithDelimiter(Integer.toString(sportId), Integer.toString(clientId), "|");
    }
}
