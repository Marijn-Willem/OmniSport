package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CompetitionKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Competition;
import com.sports.entity.manager.CompetitionManager;

import java.sql.SQLException;
import java.sql.Statement;

public class CompetitionFragment extends WritableFragment {
    private final int competitionId;

    private String name;
    private int genderId;
    private boolean h2hDouble;
    private boolean isDomestic;
    private GeoFragment geoFragment;

    private Integer geoId;
    private int sportId;

    public CompetitionFragment(int competitionId) {
        this(competitionId, 0);
    }

    public CompetitionFragment(int competitionId, int nestingLevel) {
        super(nestingLevel, true);
        this.competitionId = competitionId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new CompetitionKey(competitionId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
       Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

       name = competition.getName();
       genderId = competition.getGenderId();
       h2hDouble = competition.isH2hDouble();
       isDomestic = competition.isDomestic();
       geoId = competition.getGeoId();
       sportId = competition.getSportId();

       if (geoId != null)
           geoFragment = DataFragmentUtil.getFilledDataFragment(new GeoFragment(geoId,
                           DataFragmentUtil.getLevelForNestedFragment(nestingLevel)), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", competitionId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getGenderXML(genderId) +
                XmlUtil.getTag("h2hDouble", h2hDouble) +
                XmlUtil.getTag("isDomestic", isDomestic) +
                XmlUtil.getNullableFragmentAsTag("geo", geoFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", competitionId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getGenderJson(genderId) + "," +
                JsonUtil.getEntry("h2hDouble", h2hDouble) + "," +
                JsonUtil.getEntry("isDomestic", isDomestic) + "," +
                JsonUtil.getNullableFragmentAsEntry("geo", geoFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", competitionId, isInList) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getGenderYaml(genderId) +
                yamlUtil.getEntry("h2hDouble", h2hDouble) +
                yamlUtil.getEntry("isDomestic", isDomestic) +
                yamlUtil.getNullableFragmentAsEntry("geo", geoFragment);
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public Integer getGeoId() {
        return geoId;
    }

    public int getSportId() {
        return sportId;
    }
}
