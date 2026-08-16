package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.GeoAsParentKey;
import com.sports.cache.util.*;
import com.sports.db.type.Point;
import com.sports.entity.Geo;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.GeoManager;

import java.sql.SQLException;
import java.sql.Statement;

public class GeoAsParentFragment extends WritableFragment {
    final int geoId;
    final int clientId;
    final Integer competitionId;
    final Integer seasonId;

    private String name;
    private GeoTypeFragment geoTypeFragment;
    private Point coordinates;

    Integer parentGeoId;

    public GeoAsParentFragment(int geoId, int clientId, int nestingLevel) {
        super(nestingLevel, false);

        this.clientId = clientId;
        this.geoId = geoId;
        this.competitionId = null;
        this.seasonId = null;
    }

    public GeoAsParentFragment(int geoId, int competitionId, int seasonId, int clientId, int nestingLevel) {
        super(nestingLevel, false);

        this.geoId = geoId;
        this.clientId = clientId;
        this.competitionId = competitionId;
        this.seasonId = seasonId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new GeoAsParentKey(geoId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Geo geo = new GeoManager(stat).getEntityFromId(geoId);

        EntityInstanceUtil entityInstanceUtil = new EntityInstanceUtil(clientId, getCacheDataKey(), stat);

        if (competitionId != null && seasonId != null)
            name = entityInstanceUtil.getGeoString(geoId, new CompSeasonKey(competitionId, seasonId));
        else
            name = entityInstanceUtil.getGeoString(geoId);

        geoTypeFragment = DataFragmentUtil.getFilledDataFragment(
                new GeoTypeFragment(geo.getGeoTypeId(), YamlUtil.getLevelForNestedFragment(nestingLevel)),
                getCacheDataKey(), stat);
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

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", geoId) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getFragmentAsEntry("geoType", geoTypeFragment) +
                yamlUtil.getEntry("link", coordinates);
    }
}
