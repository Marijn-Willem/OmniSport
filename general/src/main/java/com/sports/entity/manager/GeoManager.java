package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Geo;
import com.sports.entity.GeoInstance;
import com.sports.entity.key.GeoInstanceKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class GeoManager extends InstanceEntityManager<Geo, GeoInstanceKey, GeoInstance> {
    public GeoManager(Statement stat) {
        super(stat);
    }

    @Override
    EntityInstanceManager<GeoInstanceKey, GeoInstance> getEntityInstanceManager() {
        return new GeoInstanceManager(stat);
    }

    @Override
    GeoInstance getEntityInstance() {
        return new GeoInstance();
    }

    @Override
    GeoInstanceKey getEntityInstanceKey(GeoInstance entityInstance) {
        return new GeoInstanceKey(entityInstance.getEntityId(), entityInstance.getEntityInstanceId());
    }

    public void update(int geoId, Geo geo) throws SQLException {
        super.update(geoId, geo);
    }

    public List<Geo> getAllGeosByType(int geoTypeId) throws SQLException {
        return getEntityList("geotypeid = " + geoTypeId);
    }

    String getTableName() {
        return "geo";
    }

    String[] getValueColumns() {
        return new String[] {
                "name",
                "geotypeid",
                "parentgeoid",
                "coordinates"
        };
    }

    @Override
    Geo getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Geo geo = new Geo();
        geo.setId(rs.getInt("id"));
        geo.setName(rs.getString("name"));
        geo.setGeoTypeId(rs.getInt("geotypeid"));
        geo.setParentGeoId(QueryUtil.getIntegerFromResultSet(rs, "parentgeoid"));
        geo.setCoordinates(QueryUtil.getPointFromResultSet(rs, "coordinates"));

        return geo;
    }

    public List<Geo> getGeosFromName(String name) throws SQLException {
        return getEntityList("name = " + QueryUtil.convertStringToDbValue(name));
    }

    public List<Geo> getGeosFromNameLike(String name) throws SQLException {
        return getEntityListNameLike(name);
    }

    public Map<Integer, Geo> getGeoMap(List<Integer> geoIds) throws SQLException {
        return getEntityMapFromIds(geoIds);
    }

    public List<Geo> getGeosByUniqueFields(String name, int geoTypeId, List<Integer> parentGeoIds) throws SQLException {
        String[] whereClauses = new String[Math.max(parentGeoIds.size(), 1)];

        if (parentGeoIds.isEmpty())
            whereClauses[0] = getWhereClauseGeoFields(name, geoTypeId, null);

        for (int i = 0; i < parentGeoIds.size(); i++)
            whereClauses[i] = getWhereClauseGeoFields(name, geoTypeId, parentGeoIds.get(i));

        return getEntityListFromMultipleClauses(whereClauses);
    }

    private String getWhereClauseGeoFields(String name, int geoTypeId, Integer parentGeoId) {
        return "name = " + QueryUtil.convertStringToDbValue(name) + " AND geotypeid = " + geoTypeId +
                (parentGeoId != null ? " AND parentgeoid = " + parentGeoId : "");
    }
}
