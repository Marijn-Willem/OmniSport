package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.NoCountResultKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.NoCountResult;
import com.sports.entity.manager.NoCountResultManager;

import java.sql.SQLException;
import java.sql.Statement;

public class NoCountResultFragment extends WritableFragment {
    private final int noCountResultId;

    private String name;

    public NoCountResultFragment(int noCountResultId, int nestingLevel) {
        super(nestingLevel, false);

        this.noCountResultId = noCountResultId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new NoCountResultKey(noCountResultId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        NoCountResult noCountResult = new NoCountResultManager(stat).getEntityFromId(noCountResultId);
        name = noCountResult.getName();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("name", name);
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getEntry("name", name);
    }
}
