package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EntityInstanceCompSeasonKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.EntityInstance;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class EntityInstanceCompSeasonFragment extends DataFragment {
    private final String entityName;
    private final int entityId;
    private final CompSeasonKey compSeasonKey;

    private EntityInstance entityInstance;

    public EntityInstanceCompSeasonFragment(String entityName, int entityId, CompSeasonKey compSeasonKey) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.compSeasonKey = compSeasonKey;
    }

    @Override
    public CacheKey getCacheKey() {
        return new EntityInstanceCompSeasonKey(entityName, entityId, compSeasonKey);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        LocalDateTime startDate = DataFragmentUtil.getFilledDataFragment(
                new CompSeasonFragment(compSeasonKey), getCacheDataKey(), stat).getStartDate();

        EntityInstanceFactory factory = Calculation.getEntityInstanceFactory(entityName);
        entityInstance = factory.getManager(stat).getInstanceOnDateTime(entityId, startDate);
    }

    public EntityInstance getEntityInstance() {
        return entityInstance;
    }
}
