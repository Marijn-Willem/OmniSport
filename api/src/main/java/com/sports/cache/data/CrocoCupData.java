package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CrocoCupKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.calc.teamsports.DbCalculation;
import com.sports.entity.Team;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CrocoCupData extends OutputData {
    private final int competitionId;
    private final Integer clientId;
    private final int nestingLevelNested = DataFragmentUtil.getLevelForNestedFragment(nestingLevel);

    private TeamInCrocoCupFragment holder;
    private final List<TeamInCrocoCupFragment> standing = new ArrayList<>();

    public CrocoCupData(int competitionId, Integer clientId) {
        this.competitionId = competitionId;
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new CrocoCupKey(competitionId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        List<Team> cupStanding = new DbCalculation(stat).getCrocoCupStanding(competitionId);
        int nestingLevelFragment = DataFragmentUtil.getLevelForNestedFragment(nestingLevelNested);
        int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevelNested);

        cupStanding.forEach(x -> {
            TeamInCrocoCupFragment standingFragment = new TeamInCrocoCupFragment(x, clientId, true,
                    nestingLevelList, true);
            standing.add(standingFragment);

            if (x.isHasCup())
                holder = new TeamInCrocoCupFragment(x, clientId, false, nestingLevelFragment, false);
        });

        DataFragmentUtil.fillDataFragments(standing, getCacheKey());
    }

    @Override
    public boolean isValidOutput() {
        return holder != null;
    }

    @Override
    public String toXML() {
        return XmlUtil.getOpeningTag("crocoCup") +
                XmlUtil.getNullableFragmentAsTag("holder", holder) +
                XmlUtil.getEnclosedXmlList("standing", "team", standing) +
                "</crocoCup>";
    }

    @Override
    public String toJson() {
        return "{\"crocoCup\": {" +
                JsonUtil.getNullableFragmentAsEntry("holder", holder) + "," +
                JsonUtil.getArray("standing", standing) +
                "}}";
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);
        YamlUtil yamlUtilNested = new YamlUtil(nestingLevelNested);

        return yamlUtil.getEntryHeader("crocoCup") +
                yamlUtilNested.getNullableFragmentAsEntry("holder", holder) +
                yamlUtilNested.getArray("standing", standing);
    }
}
