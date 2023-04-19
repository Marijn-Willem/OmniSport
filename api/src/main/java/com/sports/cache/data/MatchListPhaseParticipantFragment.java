package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.MatchListPhaseParticipantKey;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.H2HMatch;

import java.util.List;

public class MatchListPhaseParticipantFragment extends MatchListPhaseFragment {
    private final int participantId;

    public MatchListPhaseParticipantFragment(CompSeasonPhase compSeasonPhase, List<H2HMatch> h2HMatches,
                                             int participantId, int clientId) {
        super(compSeasonPhase, h2HMatches, clientId);
        this.participantId = participantId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new MatchListPhaseParticipantKey(competitionId, seasonId, compSeasonPhaseId, participantId);
    }
}
