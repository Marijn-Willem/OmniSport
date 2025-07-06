package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.LocationRoleKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.LocationRole;
import com.sports.entity.manager.LocationRoleManager;

import java.sql.SQLException;
import java.sql.Statement;

public class LocationRoleFragment extends WritableFragment {
    private final int locationRoleId;

    private String name;

    public LocationRoleFragment(int locationRoleId, int nestingLevel) {
        super(nestingLevel, false);

        this.locationRoleId = locationRoleId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new LocationRoleKey(locationRoleId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        LocationRole locationRole = new LocationRoleManager(stat).getEntityFromId(locationRoleId);

        name = locationRole.getName();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", locationRoleId) +
                XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", locationRoleId) + "," +
                JsonUtil.getEntry("name", name);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", locationRoleId) +
                yamlUtil.getEntry("name", name);
    }
}
