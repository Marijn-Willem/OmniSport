package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EntityInstanceCompSeasonKey;
import com.sports.cache.key.EntityInstanceNonCompSeasonKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class EntityInstanceFlusher extends CacheFlusher {
    private final String entityName;
    private final int entityId;

    public EntityInstanceFlusher(String entityName, int entityId) {
        this.entityName = entityName;
        this.entityId = entityId;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) throws SQLException {
        EntityInstanceFactory factory = Calculation.getEntityInstanceFactory(entityName);
        assert factory != null;

        List<CacheKey> cacheKeys = factory.getCompSeasonsRelatedToEntity(entityId, stat)
                .stream().map(x -> new EntityInstanceCompSeasonKey(entityName, entityId, x))
                .collect(Collectors.toList());
        cacheKeys.add(new EntityInstanceNonCompSeasonKey(entityName, entityId));

        return cacheKeys;
    }
}
