package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DartsMatchKey extends CacheDataKey {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;
    private final int clientId;

    public DartsMatchKey(int competitionId, int seasonId, int personMatchId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[]{
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(personMatchId), Integer.toString(clientId)
        }, "|");
    }
}
