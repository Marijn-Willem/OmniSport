package com.sports.entity.manager;

import com.sports.entity.LocationRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class LocationRoleManager extends IntAliasableManager<LocationRole> {
    public LocationRoleManager(Statement stat) {
        super(stat);
    }

    @Override
    LocationRole getInstance() {
        return new LocationRole();
    }

    @Override
    String getTableName() {
        return "locationrole";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    @Override
    LocationRole getInstanceFromResultSet(ResultSet rs) throws SQLException {
        LocationRole locationRole = new LocationRole();
        locationRole.setId(rs.getInt("id"));
        locationRole.setName(rs.getString("name"));

        return locationRole;
    }

    public List<LocationRole> getAllLocationRoles() throws SQLException {
        return getEntityList(null);
    }
}
