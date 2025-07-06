package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.MatchListPhaseParticipantKey;
import com.sports.entity.H2HMatch;

import java.util.List;

public class MatchListPhaseParticipantFragment extends MatchListPhaseFragment {
    private final int participantId;

    public MatchListPhaseParticipantFragment(int competitionId, int seasonId, int compSeasonPhaseId,
                                             List<H2HMatch> h2HMatches, int participantId, int clientId, int nestingLevel) {
        super(competitionId, seasonId, compSeasonPhaseId, h2HMatches, clientId, nestingLevel);
        this.participantId = participantId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new MatchListPhaseParticipantKey(competitionId, seasonId, compSeasonPhaseId, participantId);
    }
}
