package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.GeoType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoTypeManager extends IntSuperManager<GeoType> {
    public GeoTypeManager(Statement stat) {
        super(stat);
    }

    String getTableName() {
        return "geotype";
    }

    String[] getValueColumns() {
        return new String[] {
                "name"
        };
    }

    GeoType getInstanceFromResultSet(ResultSet rs) throws SQLException {
        GeoType geoType = new GeoType();

        geoType.setId(rs.getInt("id"));
        geoType.setName(rs.getString("name"));

        return geoType;
    }

    public List<GeoType> getAllGeoTypes() throws SQLException {
        return getEntityList(null);
    }

    public Map<Integer, GeoType> getAllGeoTypesAsMap() throws SQLException {
        Map<Integer, GeoType> geoTypeMap = new HashMap<Integer, GeoType>();

        List<GeoType> geoTypeList = getAllGeoTypes();

        for (GeoType geoType : geoTypeList)
            geoTypeMap.put(geoType.getId(), geoType);

        return geoTypeMap;
    }

    public GeoType getGeoTypeByName(String name) throws SQLException {
        return getEntity("name = " + QueryUtil.convertStringToDbValue(name));
    }

    public GeoType getGeoType(int geoTypeId) throws SQLException {
        return getEntityFromId(geoTypeId);
    }
}
