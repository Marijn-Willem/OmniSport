package com.sports.cache.data;

import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class StandingData extends AbstractStandingData {
    final List<StandingParticipantFragment> standingParticipants = new ArrayList<>();

    public StandingData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
        super(competitionId, seasonId, compSeasonPhaseId, clientId);
    }

    @Override
    public boolean isValidOutput() {
        return !standingParticipants.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("participantList", "participant", standingParticipants);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("participantList", standingParticipants) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("participantList", standingParticipants);
    }
}
