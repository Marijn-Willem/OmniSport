package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.GeoTypeKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.GeoType;
import com.sports.entity.manager.GeoTypeManager;

import java.sql.SQLException;
import java.sql.Statement;

public class GeoTypeFragment extends WritableFragment {
    private final int geoTypeId;

    private String name;

    public GeoTypeFragment(int geoTypeId, int nestingLevel) {
        super(nestingLevel, false);

        this.geoTypeId = geoTypeId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new GeoTypeKey(geoTypeId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        GeoType geoType = new GeoTypeManager(stat).getGeoType(geoTypeId);

        name = geoType.getName();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", geoTypeId) +
                XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", geoTypeId) + "," +
                JsonUtil.getEntry("name", name);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", geoTypeId) +
                yamlUtil.getEntry("name", name);
    }
}
