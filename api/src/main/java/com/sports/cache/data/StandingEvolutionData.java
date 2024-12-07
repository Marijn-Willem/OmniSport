package com.sports.cache.data;

import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class StandingEvolutionData extends AbstractStandingData {
    final List<StandingEvolutionSnapshotFragment> snapshotFragments = new ArrayList<>();

    public StandingEvolutionData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
        super(competitionId, seasonId, compSeasonPhaseId, clientId);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("snapshotList", "snapshot", snapshotFragments);
    }

    @Override
    public String toJson() {
        return "{ " + JsonUtil.getArray("snapshotList", snapshotFragments) + " }";
    }

    @Override
    public boolean isValidOutput() {
        return !snapshotFragments.isEmpty();
    }
}
