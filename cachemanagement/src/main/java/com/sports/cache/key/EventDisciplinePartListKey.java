package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventDisciplinePartListKey extends CacheKey {
    private final int competitionId;
    private final int seasonId;
    private final int sportId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;

    public EventDisciplinePartListKey(int competitionId, int seasonId, int sportId, int compSeasonEventId, int compSeasonEventPartId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.sportId = sportId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(sportId), Integer.toString(compSeasonEventId), Integer.toString(compSeasonEventPartId)
        }, "|");
    }
}
