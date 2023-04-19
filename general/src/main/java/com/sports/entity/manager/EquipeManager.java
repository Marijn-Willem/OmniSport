package com.sports.entity.manager;

import com.sports.entity.Equipe;
import com.sports.entity.EquipeInstance;
import com.sports.entity.key.EquipeInstanceKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EquipeManager extends InstanceEntityManager<Equipe, EquipeInstanceKey, EquipeInstance> {
    public EquipeManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "equipe";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    @Override
    EntityInstanceManager<EquipeInstanceKey, EquipeInstance> getEntityInstanceManager() {
        return new EquipeInstanceManager(stat);
    }

    @Override
    EquipeInstance getEntityInstance() {
        return new EquipeInstance();
    }

    @Override
    EquipeInstanceKey getEntityInstanceKey(EquipeInstance entityInstance) {
        return new EquipeInstanceKey(entityInstance.getEntityId(), entityInstance.getEntityInstanceId());
    }

    @Override
    Equipe getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Equipe equipe = new Equipe();

        equipe.setId(rs.getInt("id"));
        equipe.setName(rs.getString("name"));

        return equipe;
    }

    public Equipe getEquipeByName(String name) throws SQLException {
        return getEntityByName(name);
    }

    public List<Equipe> getEquipeListNameLike(String name) throws SQLException {
        return getEntityListNameLike(name);
    }
}
