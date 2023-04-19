package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SpeedSkatingHeatKey extends CacheDataKey {
    private final int competitionId;
    private final int seasonId;
    private final int sportEventId;
    private final int sportEventPartId;
    private final int heat;
    private final int clientId;

    public SpeedSkatingHeatKey(int competitionId, int seasonId, int sportEventId, int sportEventPartId, int heat, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.sportEventId = sportEventId;
        this.sportEventPartId = sportEventPartId;
        this.heat = heat;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(sportEventId), Integer.toString(sportEventPartId),
                Integer.toString(heat), Integer.toString(clientId)
        }, "|");
    }
}
