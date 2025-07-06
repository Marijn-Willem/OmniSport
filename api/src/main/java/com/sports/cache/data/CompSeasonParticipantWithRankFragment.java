package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CompSeasonParticipantWithRankKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Participant;

public class CompSeasonParticipantWithRankFragment extends CompSeasonParticipantFragment {
    private final Integer rank;

    public CompSeasonParticipantWithRankFragment(int competitionId, int seasonId, Participant participant,
                                                 int clientId, int nestingLevel) {
        super(competitionId, seasonId, participant.getId(), clientId, nestingLevel, true);
        rank = participant.getRank();
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
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

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel).getEntry("rank", rank);
    }
}
