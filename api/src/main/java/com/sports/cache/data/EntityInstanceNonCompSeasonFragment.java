package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EntityInstanceNonCompSeasonKey;
import com.sports.entity.EntityInstance;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class EntityInstanceNonCompSeasonFragment extends DataFragment {
    private final String entityName;
    private final int entityId;

    private EntityInstance entityInstance;

    public EntityInstanceNonCompSeasonFragment(String entityName, int entityId) {
        this.entityName = entityName;
        this.entityId = entityId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new EntityInstanceNonCompSeasonKey(entityName, entityId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        EntityInstanceFactory<? extends EntityInstanceKey, ? extends EntityInstance> factory =
                Calculation.getEntityInstanceFactory(entityName);

        if (factory != null)
            entityInstance = factory.getManager(stat).getInstanceOnDateTime(entityId, null);
    }

    public EntityInstance getEntityInstance() {
        return entityInstance;
    }
}
