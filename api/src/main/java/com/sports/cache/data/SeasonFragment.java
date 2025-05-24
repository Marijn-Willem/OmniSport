package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompSeason;

import java.sql.Statement;
import java.time.LocalDateTime;

public class SeasonFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final String name;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public SeasonFragment(CompSeason compSeason, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = compSeason.getCompetitionId();
        this.seasonId = compSeason.getSeasonId();
        this.name = compSeason.getSeasonName();
        this.startDate = compSeason.getStartDate();
        this.endDate = compSeason.getEndDate();
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonKey(competitionId, seasonId);
    }

    @Override
    void fill(Statement stat) {

    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", seasonId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getTag("startdate", startDate) +
                XmlUtil.getTag("enddate", endDate);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", seasonId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("startdate", startDate) + "," +
                JsonUtil.getEntry("enddate", endDate);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", seasonId, isInList) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getEntry("startdate", startDate) +
                yamlUtil.getEntry("enddate", endDate);
    }
}
