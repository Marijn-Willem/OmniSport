package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.GeoKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;

import java.sql.SQLException;
import java.sql.Statement;

public class GeoFragment extends GeoAsParentFragment {
    private GeoAsParentFragment parentGeo;

    public GeoFragment(int geoId, int nestingLevel) {
        super(geoId, nestingLevel);
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new GeoKey(geoId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);
        if (parentGeoId != null)
            parentGeo = DataFragmentUtil.getFilledDataFragment(
                    new GeoAsParentFragment(parentGeoId, YamlUtil.getLevelForNestedFragment(nestingLevel)),
                    getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getNullableFragmentAsTag("parentGeo", parentGeo);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getNullableFragmentAsEntry("parentGeo", parentGeo);
    }

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel).getNullableFragmentAsEntry("parentGeo", parentGeo);
    }
}
