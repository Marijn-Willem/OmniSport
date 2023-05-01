package com.sports.cache.key;

import com.sports.logic.util.Util;

public class EventPartParticipantKey extends CacheKey {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int participantId;

    public EventPartParticipantKey(int competitionId, int seasonId, int compSeasonEventId,
                                   int compSeasonEventPartId, int participantId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.participantId = participantId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(compSeasonEventId), Integer.toString(compSeasonEventPartId),
                Integer.toString(participantId)
        }, "|");
    }
}
