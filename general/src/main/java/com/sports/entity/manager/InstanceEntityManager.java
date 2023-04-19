package com.sports.entity.manager;

import com.sports.entity.Entity;
import com.sports.entity.EntityInstance;
import com.sports.entity.NamedIntEntity;
import com.sports.entity.key.EntityInstanceKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public abstract class InstanceEntityManager<S extends NamedIntEntity, T extends EntityInstanceKey, U extends EntityInstance>
    extends IntSuperManager<S> {
    private EntityInstanceManager<T, U> entityInstanceManager;

    abstract EntityInstanceManager<T, U> getEntityInstanceManager();
    abstract U getEntityInstance();
    abstract T getEntityInstanceKey(U entityInstance);

    public InstanceEntityManager(Statement stat) {
        super(stat);
    }

    @Override
    public void insert(int id, S entity) throws SQLException {
        super.insert(id, entity);
        retrieveEntityInstanceManager().insertInitialInstance(id, getInstanceFromEntity(entity));
    }

    @Override
    void insertIdEntities(List<S> entities, int idStart) throws SQLException {
        super.insertIdEntities(entities, idStart);
        retrieveEntityInstanceManager().insertInitialInstances(
                entities.stream().map(this::getInstanceFromEntity).collect(Collectors.toList()), idStart);
    }

    @Override
    public void update(int id, Entity entity) throws SQLException {
        super.update(id, entity);
        EntityInstanceManager<T, U> entityInstanceManager = retrieveEntityInstanceManager();

        U currentInstance = entityInstanceManager.getCurrentInstance(id);
        currentInstance.setName(((NamedIntEntity)entity).getName());
        entityInstanceManager.update(getEntityInstanceKey(currentInstance), currentInstance);
    }

    @Override
    void delete(int id) throws SQLException {
        retrieveEntityInstanceManager().deleteInstances(id);
        super.delete(id);
    }

    private EntityInstanceManager<T, U> retrieveEntityInstanceManager() {
        if (entityInstanceManager == null)
            entityInstanceManager = getEntityInstanceManager();

        return entityInstanceManager;
    }

    private U getInstanceFromEntity(S entity) {
        U entityInstance = getEntityInstance();

        entityInstance.setName(entity.getName());

        return entityInstance;
    }
}
