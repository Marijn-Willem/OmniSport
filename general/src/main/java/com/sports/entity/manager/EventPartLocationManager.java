package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.EventPartLocation;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartLocationKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventPartLocationManager extends SuperKeySuperManager<EventPartLocationKey, EventPartLocation> {
    public EventPartLocationManager(Statement stat) {
        super(stat);
    }

    @Override
    EventPartLocationKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventPartLocationKey(
                ((CompSeasonEventPartManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("eventpartlocationid"));
    }

    @Override
    String getTableName() {
        return "eventpartlocation";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", eventpartlocationid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "geoid",
                "coordinates",
                "locationroleid"
        };
    }

    @Override
    EventPartLocation getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventPartLocation eventPartLocation = new EventPartLocation();

        eventPartLocation.setCompSeasonEventPartId(rs.getInt("compseasoneventpartid"));
        eventPartLocation.setEventPartLocationId(rs.getInt("eventpartlocationid"));
        eventPartLocation.setGeoId(QueryUtil.getIntegerFromResultSet(rs, "geoid"));
        eventPartLocation.setCoordinates(QueryUtil.getPointFromResultSet(rs, "coordinates"));
        eventPartLocation.setLocationRoleId(rs.getInt("locationroleid"));

        return eventPartLocation;
    }

    @Override
    CompSeasonEventPartManager getSuperManager() {
        return new CompSeasonEventPartManager(stat);
    }

    public int getNewEventPartLocationId(CompSeasonEventPartKey compSeasonEventPartKey) throws SQLException {
        return getNewInt("eventpartlocationid", compSeasonEventPartKey.getWhereClause());
    }

    public List<EventPartLocation> getEventPartLocations(CompSeasonEventPartKey compSeasonEventPartKey)
        throws SQLException {
        return getEntityList(compSeasonEventPartKey.getWhereClause());
    }

    public List<EventPartLocation> getEventPartLocations(CompSeasonEventKey compSeasonEventKey)
        throws SQLException {
        return getEntityList(compSeasonEventKey.getWhereClause());
    }

    public List<EventPartLocationKey> getEventPartLocationsForGeo(int geoId) throws SQLException {
        return getSuperKeyList("geoid = " + geoId);
    }
}
