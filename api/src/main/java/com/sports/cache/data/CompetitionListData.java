package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompetitionListKey;
import com.sports.cache.util.*;
import com.sports.entity.Competition;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.CompetitionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompetitionListData extends OutputData {
    private final int sportId;
    private final Integer clientId;

    private final List<CompetitionFragment> competitionFragments = new ArrayList<>();

    public CompetitionListData(int sportId, Integer clientId) {
        this.sportId = sportId;
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new CompetitionListKey(sportId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        List<Competition> competitions = new CompetitionManager(stat).getCompetitionList(sportId);
        competitions.sort(new NamedEntityName());

        ClientCompetitionFilter filter = new ClientCompetitionFilter(clientId, getCacheDataKey(), stat);

        int nestingLevelList = YamlUtil.getLevelForNestedList(nestingLevel);

        competitions.stream()
                .map(x -> new CompetitionFragment(x.getId(), clientId, nestingLevelList))
                .filter(filter::isElementAllowed)
                .forEach(competitionFragments::add);

        DataFragmentUtil.fillDataFragments(competitionFragments, getCacheDataKey());
    }

    @Override
    public boolean isValidOutput() {
        return !competitionFragments.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("competitionList", "competition", competitionFragments);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("competitionList", competitionFragments) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("competitionList", competitionFragments);
    }
}
