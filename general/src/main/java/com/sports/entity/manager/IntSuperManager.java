package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Entity;
import com.sports.entity.IntEntity;
import com.sports.entity.NamedIntEntity;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

public abstract class IntSuperManager<T extends IntEntity> extends SuperManager<T> {
    @Override
    String getKeyColumnString() {
        return "id";
    }

    public IntSuperManager(Statement stat) {
        super(stat);
    }

    public void insert(int id, T entity) throws SQLException {
        String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

        String query = "INSERT INTO " + getTableName() + " (id, " +
                Util.concatStrings(getValueColumns(), ", ") + ", created, modified) " +
                "VALUES (" + id + ", " +
                Util.concatStrings(entity.getPropertiesInSQLStrings(), ", ") + ", " +
                calNowString + ", " + calNowString + ")";

        stat.execute(query);
    }

    public void update(int id, Entity entity) throws SQLException {
        stat.execute(getUpdateQuery(id, entity, QueryUtil.convertDateTimeToDbString(LocalDateTime.now())));
        processAfterUpdate(id);
    }

    public int getNewId() throws SQLException {
        return getNewInt("id", null);
    }

    public T getEntityFromId(int id) throws SQLException {
        ResultSet rs = stat.executeQuery(getGenericQuery("id = " + id));

        if (rs.next())
            return getInstanceFromResultSet(rs);

        return null;
    }

    void delete(int id) throws SQLException {
        String query = "DELETE FROM " + getTableName() + " WHERE id = " + id;
        stat.execute(query);
    }

    void update(Map<Integer, ? extends Entity> map) throws SQLException {
        if (!map.isEmpty()) {
            String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

            for (Map.Entry<Integer, ? extends Entity> me : map.entrySet())
                stat.addBatch(getUpdateQuery(me.getKey(), me.getValue(), calNowString));

            stat.executeBatch();
            stat.clearBatch();
        }

        for (Integer id : map.keySet())
            processAfterUpdate(id);
    }

    void processAfterUpdate(int id) throws SQLException { }

    void insertIdEntities(List<T> entities, int idStart) throws SQLException {
        if (!entities.isEmpty()) {
            String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

            String[] selects = new String[entities.size()];

            for (int i = 0; i < entities.size(); i++)
                selects[i] = "SELECT " + (idStart + i) + ", " +
                        Util.concatStrings(entities.get(i).getPropertiesInSQLStrings(), ", ") +
                        ", " + calNowString + ", " + calNowString;

            String query = "INSERT INTO " + getTableName() + "(id, " +
                    Util.concatStrings(getValueColumns(), ", ") + ", created, modified) " +
                    Util.concatStrings(selects, " UNION ");

            stat.execute(query);
        }
    }

    int getIdFromColumnValue(String table, String column, String colValue)
            throws SQLException {
        String query = "SELECT id FROM " + table + " WHERE " + column + " = \"" + colValue + "\"";

        ResultSet rs = stat.executeQuery(query);
        if (rs.next())
            return rs.getInt("id");

        return 0;
    }

    List<Integer> getIdList(String query) throws SQLException {
        return getIdList(getGenericQuery(query), "id");
    }

    List<T> getEntityListFromIds(Collection<Integer> idList) throws SQLException {
        List<T> entityList = new ArrayList<>();

        if (!idList.isEmpty())
            entityList = getEntityList("id IN (" + getCommaSepIntList(idList) + ")");

        return entityList;
    }

    Map<Integer, T> getEntityMapFromIds(List<Integer> idList) throws SQLException {
        Map<Integer, T> entityMap = new HashMap<>();

        if (!idList.isEmpty()) {
            ResultSet rs = stat.executeQuery(getGenericQuery("id IN (" + getCommaSepIntList(idList) + ")"));

            while (rs.next())
                entityMap.put(rs.getInt("id"), getInstanceFromResultSet(rs));
        }

        return entityMap;
    }

    Map<Integer, String> getIdNameMap() throws SQLException {
        return new HashMap<>() {{
            getEntityList(null).forEach(x -> put(x.getId(), ((NamedIntEntity)x).getName()));
        }};
    }

    Map<String, Integer> getNameIdMap() throws SQLException {
        return new HashMap<>() {{
            getEntityList(null).forEach(x -> put(((NamedIntEntity)x).getName(), x.getId()));
        }};
    }

    private String getUpdateQuery(int id, Entity entity, String calNowString) {
        return "UPDATE " + getTableName() + " SET " +
                getUpdateSQLPart(entity) + ", modified = " + calNowString +
                " WHERE id = " + id;
    }
}
