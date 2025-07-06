package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DartsSetStatsKey extends CacheFragmentKey {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;
    private final int personMatchPartId;

    public DartsSetStatsKey(int competitionId, int seasonId, int personMatchId, int personMatchPartId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
        this.personMatchPartId = personMatchPartId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(personMatchId), Integer.toString(personMatchPartId)
        }, "|");
    }
}
