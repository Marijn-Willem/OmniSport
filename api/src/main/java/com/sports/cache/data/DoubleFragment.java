package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.DoubleKey;
import com.sports.cache.util.*;
import com.sports.entity.Double;
import com.sports.entity.key.CompSeasonDoubleKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.DoubleManager;

import java.sql.SQLException;
import java.sql.Statement;

public class DoubleFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int doubleId;
    private final int clientId;

    private String description;
    private PersonSportFragment personSport1Fragment;
    private PersonSportFragment personSport2Fragment;
    private int elo;

    public DoubleFragment(int competitionId, int seasonId, int doubleId, int clientId, int nestingLevel) {
        super(nestingLevel, false);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.doubleId = doubleId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new DoubleKey(new CompSeasonDoubleKey(new CompSeasonKey(competitionId, seasonId), doubleId));
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        CompSeasonDoubleKey compSeasonDoubleKey = new CompSeasonDoubleKey(compSeasonKey, doubleId);
        Double dbl = new DoubleManager(stat).getDouble(compSeasonDoubleKey.getDoubleId());

        int nestingLevelFragment = YamlUtil.getLevelForNestedFragment(nestingLevel);

        personSport1Fragment = DataFragmentUtil.getFilledDataFragment(
                new PersonSportFragment(competitionId, seasonId, dbl.getPersonSport1Id(), clientId, nestingLevelFragment, false),
                getCacheDataKey(), stat);
        personSport2Fragment = DataFragmentUtil.getFilledDataFragment(
                new PersonSportFragment(competitionId, seasonId, dbl.getPersonSport2Id(), clientId, nestingLevelFragment, false),
                getCacheDataKey(), stat);
        description = new DescribedEntityUtil(clientId, getCacheDataKey(), stat).getDoubleString(dbl, compSeasonKey);
        elo = dbl.getElo();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", doubleId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getFragmentAsTag("personSport1", personSport1Fragment) +
                XmlUtil.getFragmentAsTag("personSport2", personSport2Fragment) +
                XmlUtil.getTag("elo", elo);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", doubleId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getFragmentAsEntry("personSport1", personSport1Fragment) + "," +
                JsonUtil.getFragmentAsEntry("personSport2", personSport2Fragment) + "," +
                JsonUtil.getEntry("elo", elo);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", doubleId) +
                yamlUtil.getEntry("description", description) +
                yamlUtil.getFragmentAsEntry("personSport1", personSport1Fragment) +
                yamlUtil.getFragmentAsEntry("personSport2", personSport2Fragment) +
                yamlUtil.getEntry("elo", elo);
    }
}
