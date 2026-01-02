package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.SportDisciplineKey;
import com.sports.cache.util.*;
import com.sports.entity.SportDiscipline;
import com.sports.entity.manager.SportDisciplineManager;

import java.sql.SQLException;
import java.sql.Statement;

public class SportDisciplineFragment extends WritableFragment {
    private final int sportId;
    private final int sportDisciplineId;
    private final int clientId;

    private String name;
    private ResultTypeFragment resultTypeFragment;

    public SportDisciplineFragment(int sportId, int sportDisciplineId, int clientId, int nestingLevel) {
        super(nestingLevel, false);

        this.sportId = sportId;
        this.sportDisciplineId = sportDisciplineId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new SportDisciplineKey(sportId, sportDisciplineId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.SportDisciplineKey sdKey =
                new com.sports.entity.key.SportDisciplineKey(sportId, sportDisciplineId);

        SportDiscipline sportDiscipline = new SportDisciplineManager(stat).getEntityFromSuperKey(sdKey);

        name = new AliasUtil(clientId, getCacheDataKey(), stat)
                .getAliasableAsClientSpecificString(sportDiscipline, sdKey);

        resultTypeFragment = DataFragmentUtil.getFilledDataFragment(new ResultTypeFragment(
                sportDiscipline.getResultTypeId(), YamlUtil.getLevelForNestedFragment(nestingLevel)),
                getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("sportDisciplineId", sportDisciplineId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getFragmentAsTag("resultType", resultTypeFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("sportDisciplineId", sportDisciplineId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getFragmentAsEntry("resultType", resultTypeFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("sportDisciplineId", sportDisciplineId) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getFragmentAsEntry("resultType", resultTypeFragment);
    }
}
