package com.sports.cache.key;

import com.sports.logic.util.Util;

public class SpSkHeatPersonSportKey extends CacheKey {
    private final int competitionId;
    private final int seasonId;
    private final int sportEventId;
    private final int sportEventPartId;
    private final int heat;
    private final int personSportId;

    public SpSkHeatPersonSportKey(int competitionId, int seasonId, int sportEventId, int sportEventPartId,
                                  int heat, int personSportId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.sportEventId = sportEventId;
        this.sportEventPartId = sportEventPartId;
        this.heat = heat;
        this.personSportId = personSportId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(sportEventId), Integer.toString(sportEventPartId),
                Integer.toString(heat), Integer.toString(personSportId)
        }, "|");
    }
}
