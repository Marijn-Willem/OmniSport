package com.sports.entity.manager;

import com.sports.entity.AliasEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AliasEntityManager extends IntSuperManager<AliasEntity> {
    public AliasEntityManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "aliasentity";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    AliasEntity getInstanceFromResultSet(ResultSet rs) throws SQLException {
        AliasEntity aliasEntity = new AliasEntity();

        aliasEntity.setId(rs.getInt("id"));
        aliasEntity.setName(rs.getString("name"));

        return aliasEntity;
    }

    public List<AliasEntity> getAllAliasEntities() throws SQLException {
        return getEntityList(null);
    }
}
