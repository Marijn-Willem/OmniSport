package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.GeoAsParentKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.db.type.Point;
import com.sports.entity.Geo;
import com.sports.entity.manager.GeoManager;

import java.sql.SQLException;
import java.sql.Statement;

public class GeoAsParentFragment extends WritableFragment {
    final int geoId;

    private String name;
    private GeoTypeFragment geoTypeFragment;
    private Point coordinates;

    Integer parentGeoId;

    public GeoAsParentFragment(int geoId) {
        this.geoId = geoId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new GeoAsParentKey(geoId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Geo geo = new GeoManager(stat).getEntityFromId(geoId);

        name = geo.getName();
        geoTypeFragment = DataFragmentUtil.getFilledDataFragment(
                new GeoTypeFragment(geo.getGeoTypeId()), getCacheDataKey(), stat);
        coordinates = geo.getCoordinates();

        parentGeoId = geo.getParentGeoId();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", geoId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getFragmentAsTag("geoType", geoTypeFragment) +
                XmlUtil.getTag("link", coordinates);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", geoId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getFragmentAsEntry("geoType", geoTypeFragment) + "," +
                JsonUtil.getEntry("link", coordinates);
    }
}
