package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.MatchListPhaseParticipantKey;
import com.sports.entity.H2HMatch;

import java.util.List;

public class MatchListPhaseParticipantFragment extends MatchListPhaseFragment {
    private final int participantId;

    public MatchListPhaseParticipantFragment(int competitionId, int seasonId, int compSeasonPhaseId,
                                             List<H2HMatch> h2HMatches, int participantId, int clientId) {
        super(competitionId, seasonId, compSeasonPhaseId, h2HMatches, clientId);
        this.participantId = participantId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new MatchListPhaseParticipantKey(competitionId, seasonId, compSeasonPhaseId, participantId);
    }
}
