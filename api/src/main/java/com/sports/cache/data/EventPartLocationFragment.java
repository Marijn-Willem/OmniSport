package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.EventPartLocationKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.db.type.Point;
import com.sports.entity.EventPartLocation;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.EventPartLocationManager;

import java.sql.SQLException;
import java.sql.Statement;

public class EventPartLocationFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int eventPartLocationId;

    private GeoFragment geoFragment;
    private Point coordinates;
    private LocationRoleFragment locationRoleFragment;

    public EventPartLocationFragment(int competitionId, int seasonId, int compSeasonEventId,
                                     int compSeasonEventPartId, int eventPartLocationId, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.eventPartLocationId = eventPartLocationId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new EventPartLocationKey(competitionId, seasonId, compSeasonEventId,
                compSeasonEventPartId, eventPartLocationId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.EventPartLocationKey eplKey = new com.sports.entity.key.EventPartLocationKey(
                new CompSeasonEventPartKey(
                        new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), compSeasonEventId),
                        compSeasonEventPartId),
                eventPartLocationId
        );

        EventPartLocation eventPartLocation = new EventPartLocationManager(stat).getEntityFromSuperKey(eplKey);

        int nestingLevelFragment = YamlUtil.getLevelForNestedFragment(nestingLevel);

        if (eventPartLocation.getGeoId() != null)
            geoFragment = DataFragmentUtil.getFilledDataFragment(new GeoFragment(eventPartLocation.getGeoId(),
                            nestingLevelFragment), getCacheDataKey(), stat);

        coordinates = eventPartLocation.getCoordinates();
        locationRoleFragment = DataFragmentUtil.getFilledDataFragment(
                new LocationRoleFragment(eventPartLocation.getLocationRoleId(), nestingLevelFragment),
                getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("eventPartLocationId", eventPartLocationId) +
                XmlUtil.getNullableFragmentAsTag("geo", geoFragment) +
                XmlUtil.getTag("link", coordinates) +
                XmlUtil.getFragmentAsTag("locationRole", locationRoleFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("eventPartLocationId", eventPartLocationId) + "," +
                JsonUtil.getNullableFragmentAsEntry("geo", geoFragment) + "," +
                JsonUtil.getEntry("link", coordinates) + "," +
                JsonUtil.getFragmentAsEntry("locationRole", locationRoleFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("eventPartLocationId", eventPartLocationId, isInList) +
                yamlUtil.getNullableFragmentAsEntry("geo", geoFragment) +
                yamlUtil.getEntry("link", coordinates) +
                yamlUtil.getFragmentAsEntry("locationRole", locationRoleFragment);
    }
}
