package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonPhaseListKey;
import com.sports.cache.util.*;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.comparator.CompSeasonPhaseRoundDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonPhaseManager;
import com.sports.calc.h2hsports.DbCalculation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompSeasonPhaseListData extends OutputData {
    private final int competitionId;
    private final int seasonId;
    private final Integer clientId;

    private final List<CompSeasonPhaseFragment> compSeasonPhaseFragments = new ArrayList<>();

    public CompSeasonPhaseListData(int competitionId, int seasonId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.clientId = clientId;
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        List<CompSeasonPhase> compSeasonPhases = new CompSeasonPhaseManager(stat).getCompSeasonPhases(compSeasonKey);
        new DbCalculation(stat).setPhaseDescriptionsFromTypes(compSeasonPhases);
        compSeasonPhases.sort(new CompSeasonPhaseRoundDescription());
        compSeasonPhases.forEach(x -> compSeasonPhaseFragments.add(
                new CompSeasonPhaseFragment(x, clientId, YamlUtil.getLevelForNestedList(nestingLevel), true)));

        DataFragmentUtil.fillDataFragments(compSeasonPhaseFragments, getCacheDataKey());
    }

    @Override
    public boolean isValidOutput() {
        return !compSeasonPhaseFragments.isEmpty();
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new CompSeasonPhaseListKey(competitionId, seasonId, clientId);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("compSeasonPhaseList", "compSeasonPhase",
                compSeasonPhaseFragments);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("compSeasonPhaseList", compSeasonPhaseFragments) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("compSeasonPhaseList", compSeasonPhaseFragments);
    }
}
