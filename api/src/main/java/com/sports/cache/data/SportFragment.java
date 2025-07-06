package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.SportKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Sport;
import com.sports.entity.manager.SportManager;

import java.sql.SQLException;
import java.sql.Statement;

public class SportFragment extends WritableFragment {
    private final int sportId;
    private final int clientId;

    private String name;
    private boolean isTeam;
    private boolean isH2H;

    public SportFragment(int sportId, int clientId, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        this.sportId = sportId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new SportKey(sportId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Sport sport = new SportManager(stat).getSport(sportId);

        name = new AliasUtil(clientId, getCacheDataKey(), stat).getAliasableAsClientSpecificString(sport);
        isTeam = sport.isTeam();
        isH2H = sport.isH2H();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", sportId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getTag("isteam", isTeam) +
                XmlUtil.getTag("ish2h", isH2H);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", sportId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("isteam", isTeam) + "," +
                JsonUtil.getEntry("ish2h", isH2H);
    }

    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", sportId, isInList) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getEntry("isteam", isTeam) +
                yamlUtil.getEntry("ish2h", isH2H);
    }

    public int getSportId() {
        return sportId;
    }
}
