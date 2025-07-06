package com.sports.cache.key;

import com.sports.logic.util.Util;

public class H2HMatchPartKey extends CacheFragmentKey {
    private final int competitionId;
    private final int seasonId;
    private final int matchId;
    private final int specificId;

    public H2HMatchPartKey(int competitionId, int seasonId, int matchId, int specificId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.matchId = matchId;
        this.specificId = specificId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(matchId), Integer.toString(specificId)
        }, "|");
    }
}
