package com.sports.cache.key;

import com.sports.logic.util.Util;

public class CompSeasonParticipantKey extends CacheKey {
    private final int competitionId;
    private final int seasonId;
    private final int participantId;

    public CompSeasonParticipantKey(int competitionId, int seasonId, int participantId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.participantId = participantId;
    }

    @Override
    public String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(participantId)
        }, "|");
    }
}
