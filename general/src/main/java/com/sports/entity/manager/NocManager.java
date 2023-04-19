package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Noc;
import com.sports.entity.NocInstance;
import com.sports.entity.key.NocInstanceKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class NocManager extends InstanceEntityManager<Noc, NocInstanceKey, NocInstance> {
    public NocManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "noc";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
            "name",
            "geoid"
        };
    }

    @Override
    Noc getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Noc noc = new Noc();

        noc.setId(rs.getInt("id"));
        noc.setName(rs.getString("name"));
        noc.setGeoId(QueryUtil.getIntegerFromResultSet(rs, "geoid"));

        return noc;
    }

    @Override
    EntityInstanceManager<NocInstanceKey, NocInstance> getEntityInstanceManager() {
        return new NocInstanceManager(stat);
    }

    @Override
    NocInstanceKey getEntityInstanceKey(NocInstance entityInstance) {
        return new NocInstanceKey(entityInstance.getEntityId(), entityInstance.getEntityInstanceId());
    }

    @Override
    NocInstance getEntityInstance() {
        return new NocInstance();
    }

    public List<Noc> getAllNocs() throws SQLException {
        return getEntityList(null);
    }

    public List<Noc> getNocsFromGeo(int geoId) throws SQLException {
        return getEntityList("geoid = " + geoId);
    }
}
