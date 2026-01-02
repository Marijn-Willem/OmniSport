package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CompSeasonEventKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonKey;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonEventFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int clientId;

    private final int genderId;
    private final int sportId;
    private final int sportEventId;
    private SportEventFragment sportEventFragment;

    public CompSeasonEventFragment(CompSeasonKey compSeasonKey, CompSeasonEvent compSeasonEvent, int clientId, int nestingLevel) {
        super(nestingLevel, true);

        competitionId = compSeasonKey.getCompetitionId();
        seasonId = compSeasonKey.getSeasonId();
        compSeasonEventId = compSeasonEvent.getCompSeasonEventId();
        this.clientId = clientId;

        genderId = compSeasonEvent.getGenderId();
        sportId = compSeasonEvent.getSportEventKey().getSportId();
        sportEventId = compSeasonEvent.getSportEventKey().getSportEventId();
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new CompSeasonEventKey(competitionId, seasonId, compSeasonEventId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        sportEventFragment = DataFragmentUtil.getFilledDataFragment(new SportEventFragment(sportId, sportEventId, clientId,
                        YamlUtil.getLevelForNestedFragment(nestingLevel)), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("compSeasonEventId", compSeasonEventId) +
                XmlUtil.getGenderXML(genderId) +
                XmlUtil.getFragmentAsTag("sportEvent", sportEventFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("compSeasonEventId", compSeasonEventId) + "," +
                JsonUtil.getGenderJson(genderId) + "," +
                JsonUtil.getFragmentAsEntry("sportEvent", sportEventFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("compSeasonEventId", compSeasonEventId, isInList) +
                yamlUtil.getGenderYaml(genderId) +
                yamlUtil.getFragmentAsEntry("sportEvent", sportEventFragment);
    }
}
