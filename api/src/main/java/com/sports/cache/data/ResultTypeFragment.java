package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.ResultTypeKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.ResultType;

import java.sql.Statement;

public class ResultTypeFragment extends WritableFragment {
    private final int resultTypeId;

    private String name;

    public ResultTypeFragment(int resultTypeId, int nestingLevel) {
        super(nestingLevel, false);

        this.resultTypeId = resultTypeId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new ResultTypeKey(resultTypeId);
    }

    @Override
    void fill(Statement stat) {
        name = ResultType.resultTypeLinkedHashMap.get(resultTypeId);
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
