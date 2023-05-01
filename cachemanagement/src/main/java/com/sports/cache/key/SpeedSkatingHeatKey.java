package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SpeedSkatingHeatKey extends CacheDataKey {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int heat;
    private final int clientId;

    public SpeedSkatingHeatKey(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId, int heat, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.heat = heat;
        this.clientId = clientId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(compSeasonEventId), Integer.toString(compSeasonEventPartId),
                Integer.toString(heat), Integer.toString(clientId)
        }, "|");
    }
}
