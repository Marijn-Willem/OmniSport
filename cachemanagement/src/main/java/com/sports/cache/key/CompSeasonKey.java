package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonKey extends CacheFragmentKey {
    private final int competitionId;
    private final int seasonId;

    public CompSeasonKey(int competitionId, int seasonId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
    }

    @Override
    public String getSpecificKeyPart() {
        return Util.concatStringsWithDelimiter(Integer.toString(competitionId), Integer.toString(seasonId), "|");
    }
}
