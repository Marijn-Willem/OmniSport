package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.SeasonListKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompSeason;
import com.sports.entity.Season;
import com.sports.entity.comparator.OrderableOrder;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonManager;
import com.sports.entity.manager.SeasonManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeasonListData extends OutputData {
    private final int competitionId;
    private final Integer clientId;

    private final List<SeasonFragment> seasonFragments = new ArrayList<>();

    public SeasonListData(int competitionId, Integer clientId) {
        this.competitionId = competitionId;
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new SeasonListKey(competitionId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        boolean isAdmin = DataFragmentUtil.getFilledOutputData(new ClientListData(), null).isAdmin(clientId);
        ClientCompSeasonData ccsData = !isAdmin ? DataFragmentUtil.getFilledOutputData(
                new ClientCompSeasonData(clientId), null) : null;

        Map<CompSeasonKey, CompSeason> csMap = new CompSeasonManager(stat)
                .getCompSeasonMapForCompetition(competitionId)
                .entrySet().stream()
                .filter(e -> isAdmin || ccsData.hasCompSeason(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Integer> seasonIds = csMap.keySet().stream().map(CompSeasonKey::getSeasonId).collect(Collectors.toList());
        List<Season> seasons = new SeasonManager(stat).getSeasonList(seasonIds);
        seasons.sort(new OrderableOrder());

        int nestingLevelList = YamlUtil.getLevelForNestedList(nestingLevel);

        seasons.forEach(x -> {
            CompSeason compSeason = csMap.get(new CompSeasonKey(competitionId, x.getId()));
            compSeason.setSeasonName(x.getName());
            seasonFragments.add(new SeasonFragment(compSeason, nestingLevelList));
        });

        DataFragmentUtil.fillDataFragments(seasonFragments, getCacheDataKey());
    }

    @Override
    public boolean isValidOutput() {
        return !seasonFragments.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("seasonList", "season", seasonFragments);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("seasonList", seasonFragments) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("seasonList", seasonFragments);
    }
}
