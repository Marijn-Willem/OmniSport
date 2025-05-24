package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompDivision;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.manager.CompDivisionManager;

import java.sql.SQLException;
import java.sql.Statement;

public class CompDivisionFragment extends WritableFragment {
    final int competitionId;
    final int compDivisionId;

    public CompDivisionFragment(int competitionId, int compDivisionId, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);
        
        this.competitionId = competitionId;
        this.compDivisionId = compDivisionId;
    }

    private String name;
    private Integer parentDivisionId;

    @Override
    void fill(Statement stat) throws SQLException {
        CompDivision compDivision = new CompDivisionManager(stat).getCompDivision(
                new CompDivisionKey(competitionId, compDivisionId));

        name = compDivision.getName();
        parentDivisionId = compDivision.getParentDivisionId();
    }

    @Override
    public CacheKey getCacheKey() {
        return new com.sports.cache.key.CompDivisionKey(new CompDivisionKey(competitionId, compDivisionId));
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("compDivisionId", compDivisionId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getTag("parentDivisionId", parentDivisionId);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("compDivisionId", compDivisionId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("parentDivisionId", parentDivisionId);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("compDivisionId", compDivisionId, isInList) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getEntry("parentDivisionId", parentDivisionId);
    }
}
