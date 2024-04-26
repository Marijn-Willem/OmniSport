package com.sports.cache.util;

import com.sports.cache.data.EntityInstanceCompSeasonFragment;
import com.sports.cache.data.EntityInstanceNonCompSeasonFragment;
import com.sports.cache.key.CacheDataKey;
import com.sports.entity.EntityInstance;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.EntityInstanceKey;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.factory.EntityInstanceFactory;

import java.sql.SQLException;
import java.sql.Statement;

public record EntityInstanceUtil(int clientId, CacheDataKey cacheDataKey, Statement stat) {
    public String getGeoString(int geoId, CompSeasonKey compSeasonKey) throws SQLException {
        return getEntityInstanceString("Geo", geoId, compSeasonKey);
    }

    public String getClubString(int clubId) throws SQLException {
        return getEntityInstanceString("Club", clubId);
    }

    public String getClubString(int clubId, CompSeasonKey compSeasonKey) throws SQLException {
        return getEntityInstanceString("Club", clubId, compSeasonKey);
    }

    public String getNocString(int nocId) throws SQLException {
        return getEntityInstanceString("Noc", nocId);
    }

    public String getNocString(int nocId, CompSeasonKey compSeasonKey) throws SQLException {
        return getEntityInstanceString("Noc", nocId, compSeasonKey);
    }

    public String getPersonString(int personId, CompSeasonKey compSeasonKey) throws SQLException {
        return getEntityInstanceString("Person", personId, compSeasonKey);
    }

    public String getEquipeString(int equipeId, CompSeasonKey compSeasonKey) throws SQLException {
        return getEntityInstanceString("Equipe", equipeId, compSeasonKey);
    }

    private String getEntityInstanceString(String entityName, int entityId) throws SQLException {
        EntityInstance entityInstance = DataFragmentUtil.getFilledDataFragment(
                        new EntityInstanceNonCompSeasonFragment(entityName, entityId), cacheDataKey, stat)
                .getEntityInstance();

        return getAliasString(entityName, entityInstance);
    }

    private String getEntityInstanceString(String entityName, int entityId, CompSeasonKey compSeasonKey)
            throws SQLException {
        EntityInstance entityInstance = DataFragmentUtil.getFilledDataFragment(
                        new EntityInstanceCompSeasonFragment(entityName, entityId, compSeasonKey), cacheDataKey, stat)
                .getEntityInstance();

        return getAliasString(entityName, entityInstance);
    }

    private String getAliasString(String entityName, EntityInstance entityInstance)
            throws SQLException {
        EntityInstanceFactory<? extends EntityInstanceKey, ? extends EntityInstance> factory =
                Calculation.getEntityInstanceFactory(entityName);
        assert factory != null;

        EntityInstanceKey entityInstanceKey = factory.getKey(entityInstance.getEntityId(),
                entityInstance.getEntityInstanceId());

        return new AliasUtil(clientId, cacheDataKey, stat)
                .getAliasableAsClientSpecificString(entityInstance, entityInstanceKey);
    }
}
