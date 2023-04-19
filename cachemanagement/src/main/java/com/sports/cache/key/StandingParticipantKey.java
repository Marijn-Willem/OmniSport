package com.sports.cache.key;

import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.logic.util.Util;

public class StandingParticipantKey extends CacheKey {
    private final int competitionId;
    private final int seasonId;
    private final int participantId;

    public StandingParticipantKey(int competitionId, int seasonId, int participantId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.participantId = participantId;
    }

    public StandingParticipantKey(CompSeasonParticipantKey compSeasonParticipantKey) {
        this.competitionId = compSeasonParticipantKey.getSuperKey().getCompetitionId();
        this.seasonId = compSeasonParticipantKey.getSuperKey().getSeasonId();
        this.participantId = compSeasonParticipantKey.getSpecificId();
    }

    @Override
    public String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(competitionId), Integer.toString(seasonId),
                Integer.toString(participantId)
        }, "|");
    }
}
