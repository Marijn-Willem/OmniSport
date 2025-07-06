package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DartsMatchStatsKey extends CacheFragmentKey {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;

    public DartsMatchStatsKey(int competitionId, int seasonId, int personMatchId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[]{
                Integer.toString(competitionId), Integer.toString(seasonId), Integer.toString(personMatchId)
        }, "|");
    }
}
