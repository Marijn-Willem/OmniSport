package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventDisciplinePartListKey extends CacheKey {
    private final int competitionId;
    private final int seasonId;
    private final int sportId;
    private final int sportEventId;
    private final int sportEventPartId;

    public EventDisciplinePartListKey(int competitionId, int seasonId, int sportId, int sportEventId, int sportEventPartId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.sportId = sportId;
        this.sportEventId = sportEventId;
        this.sportEventPartId = sportEventPartId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(sportId), Integer.toString(sportEventId), Integer.toString(sportEventPartId)
        }, "|");
    }
}
