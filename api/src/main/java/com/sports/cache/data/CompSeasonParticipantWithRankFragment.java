package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonParticipantWithRankKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Participant;

public class CompSeasonParticipantWithRankFragment extends CompSeasonParticipantFragment {
    private final Integer rank;

    public CompSeasonParticipantWithRankFragment(int competitionId, int seasonId, Participant participant, int clientId) {
        super(competitionId, seasonId, participant.getId(), clientId);
        rank = participant.getRank();
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonParticipantWithRankKey(competitionId, seasonId, participantId);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getTag("rank", rank);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getEntry("rank", rank);
    }
}
