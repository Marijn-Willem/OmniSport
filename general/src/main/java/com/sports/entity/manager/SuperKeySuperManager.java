package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.EntityKeyWithParent;
import com.sports.entity.key.SuperKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

public abstract class SuperKeySuperManager<S extends SuperKey, T extends SuperKeyEntity> extends SuperManager<T> {
    abstract S getSuperKeyFromResultSet(ResultSet rs) throws SQLException;
    private SuperKeySuperManager superManager;

    public SuperKeySuperManager(Statement stat) {
        super(stat);
    }

    SuperKeySuperManager getSuperManager() {
        return null;
    }

    SuperKeySuperManager getCachedSuperManager() {
        if (superManager == null)
            superManager = getSuperManager();

        return superManager;
    }

    public T getEntityFromSuperKey(S superKey) throws SQLException {
        ResultSet rs = stat.executeQuery(getGenericQuery(superKey.getWhereClause()));

        if (rs.next())
            return getInstanceFromResultSet(rs);

        return null;
    }

    public void insert(S superKey, T entity) throws SQLException {
        String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

        String query = "INSERT INTO " + getTableName() + " (" + getKeyColumnString() + ", " +
                Util.concatStrings(getValueColumns(), ", ") + ", created, modified) " +
                "VALUES (" + superKey.getCommaSepValues() + ", " +
                Util.concatStrings(entity.getPropertiesInSQLStrings(), ", ") + ", " +
                calNowString + ", " + calNowString + ")";

        stat.execute(query);
    }

    public void update(S superKey, T entity) throws SQLException {
        stat.execute(getUpdateQuery(superKey, entity, QueryUtil.convertDateTimeToDbString(LocalDateTime.now())));
    }

    <K extends SuperKey> void delete(Collection<K> superKeys) throws SQLException {
        if (superKeys.size() > 0)
            delete(getConditionsKeyList(superKeys));
    }

    void insert(Map<S, T> insertMap) throws SQLException {
        if (insertMap.size() > 0) {
            String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

            String[] selects = new String[insertMap.size()];

            int indX = 0;

            for (Map.Entry<S, T> me : insertMap.entrySet())
                selects[indX++] = "SELECT " + me.getKey().getCommaSepValues() + ", " +
                        Util.concatStrings(me.getValue().getPropertiesInSQLStrings(), ", ") +
                        ", " + calNowString + ", " + calNowString;

            String query = "INSERT INTO " + getTableName() + " (" + getKeyColumnString() + ", " +
                    Util.concatStrings(getValueColumns(), ", ") + ", created, modified) " +
                    Util.concatStrings(selects, " UNION ");

            stat.execute(query);
        }
    }

    List<S> getSuperKeyList(String whereClause) throws SQLException {
        List<S> superKeys = new ArrayList<>();

        ResultSet rs = stat.executeQuery(getGenericQuery(whereClause));

        while (rs.next())
            superKeys.add(getSuperKeyFromResultSet(rs));

        return superKeys;
    }

    Map<S, T> getSuperKeyEntityMap(String whereClause) throws SQLException {
        Map<S, T> map = new HashMap<>();

        ResultSet rs = stat.executeQuery(getGenericQuery(whereClause));

        while (rs.next())
            map.put(getSuperKeyFromResultSet(rs), getInstanceFromResultSet(rs));

        return map;
    }

    <K extends SuperKey> Map<S, T> getSuperKeyEntityMapFromSuperKeys(List<K> superKeys) throws SQLException {
        Map<S, T> map = new HashMap<>();

        if (superKeys.size() > 0)
            map = getSuperKeyEntityMap(getConditionsKeyList(superKeys));

        return map;
    }

    void insertNonExistingKeys(Set<S> keys) throws SQLException {
        Set<S> existingKeys = new HashSet<>(getSuperKeyList(getConditionsKeyList(keys)));

        insert(Util.getElementsLeftNotInRight(keys, existingKeys));
    }

    void insertNonExistingEntries(Map<S, T> entityMap) throws SQLException {
        Set<S> keys = entityMap.keySet();
        Set<S> existingKeys = new HashSet<>(getSuperKeyList(getConditionsKeyList(keys)));
        Map<S, T> insertMap = new HashMap<>() {{
            Util.getElementsLeftNotInRight(keys, existingKeys).forEach(x -> put(x, entityMap.get(x)));
        }};

        insert(insertMap);
    }

    <K extends EntityKeyWithParent> List<T> getChildEntities(List<K> keyList) throws SQLException {
        return getChildEntities(keyList, null);
    }

    <K extends EntityKeyWithParent> List<T> getChildEntities(List<K> keyList, String whereCondition) throws SQLException {
        List<T> entityList = new ArrayList<>();

        if (keyList.size() > 0) {
            String whereClause = "(" + getConditionsKeyListParent(keyList) + ")";
            whereClause = Util.concatStringsWithDelimiter(whereClause, whereCondition, " AND ");

            ResultSet rs = stat.executeQuery(getGenericQuery(whereClause));

            while (rs.next())
                entityList.add(getInstanceFromResultSet(rs));
        }

        return entityList;
    }
}
