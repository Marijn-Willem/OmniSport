package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Entity;
import com.sports.entity.key.EntityKeyWithParent;
import com.sports.entity.key.SuperKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class SuperManager<T extends Entity> {
    Statement stat;

    public SuperManager(Statement stat) {
        this.stat = stat;
    }

    abstract String getTableName();
    abstract String getKeyColumnString();
    abstract String[] getValueColumns();
    abstract T getInstanceFromResultSet(ResultSet rs) throws SQLException;

    String getSelectColumnString() {
        int valColsLength = getValueColumns().length;
        String[] allColumns = new String[1 + valColsLength];
        allColumns[0] = getKeyColumnString();
        System.arraycopy(getValueColumns(), 0, allColumns, 1, valColsLength);

        return Util.concatStrings(allColumns, ", ");
    }

    void insert(SuperKey superKey) throws SQLException {
        String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

        String query = "INSERT INTO " + getTableName() + " (" + getKeyColumnString() +
                ", created, modified) VALUES (" + superKey.getCommaSepValues() + ", " +
                calNowString + ", " + calNowString + ")";

        stat.execute(query);
    }

    void insert(List<? extends SuperKey> lst) throws SQLException {
        if (lst.size() > 0) {
            String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

            String[] queryParts = new String[lst.size()];
            for (int i = 0; i < lst.size(); i++)
                queryParts[i] = "SELECT " + lst.get(i).getCommaSepValues() + ", " +
                        calNowString + ", " + calNowString;

            String query = "INSERT INTO " + getTableName() + " (" + getKeyColumnString() +
                    ", created, modified) " + Util.concatStrings(queryParts, " UNION ALL ");

            stat.execute(query);
        }
    }

    void updateEntityMap(Map<? extends SuperKey, ? extends Entity> map) throws SQLException {
        if (map.size() > 0) {
            String calNowString = QueryUtil.convertDateTimeToDbString(LocalDateTime.now());

            for (Map.Entry<? extends SuperKey, ? extends Entity> me : map.entrySet())
                stat.addBatch(getUpdateQuery(me.getKey(), me.getValue(), calNowString));

            stat.executeBatch();
            stat.clearBatch();
        }
    }

    int getNewInt(String colName, String whereClause) throws SQLException {
        String query = "SELECT MAX(" + colName + ") AS maxVal FROM " + getTableName() +
                (whereClause != null ? " WHERE " + whereClause : "");

        ResultSet rs = stat.executeQuery(query);
        if (rs.next())
            return rs.getInt("maxVal") + 1;

        return 1;
    }

    List<Integer> getIdList(String query, String idCol) throws SQLException {
        List<Integer> ids = new ArrayList<>();

        ResultSet rs = stat.executeQuery(query);
        while (rs.next())
            ids.add(rs.getInt(idCol));

        return ids;
    }

    T getEntity(String whereClause) throws SQLException {
        ResultSet rs = stat.executeQuery(getGenericQuery(whereClause));

        if (rs.next())
            return getInstanceFromResultSet(rs);

        return null;
    }

    T getEntityByName(String name) throws SQLException {
        return getEntity("name = " + QueryUtil.convertStringToDbValue(name));
    }

    List<T> getEntityListNameLike(String name) throws SQLException {
        return getEntityList("name LIKE " + QueryUtil.convertStringToDbValue("%" + name + "%"));
    }

    List<T> getEntityListFromMultipleClauses(String[] whereClauses) throws SQLException {
        List<T> entityList = new ArrayList<>();

        if (whereClauses.length > 0) {
            String[] queries = new String[whereClauses.length];

            for (int i = 0; i < whereClauses.length; i++)
                queries[i] = getGenericQuery(whereClauses[i]);

            ResultSet rs = stat.executeQuery(Util.concatStrings(queries, " UNION ALL "));

            while (rs.next())
                entityList.add(getInstanceFromResultSet(rs));
        }

        return entityList;
    }

    void delete(String whereClause) throws SQLException {
        String query = "DELETE FROM " + getTableName() + (whereClause != null ? " WHERE " + whereClause : "");
        stat.execute(query);
    }

    String getCommaSepIntList(Collection<Integer> intList) {
        return Util.getCommaSepIntList(intList);
    }

    String getCommaSepStringList(List<String> strList) {
        String commaSepList = "";

        for (String str : strList)
            commaSepList += ("".equals(commaSepList) ? "" : ", ") + QueryUtil.convertStringToDbValue(str);

        return commaSepList;
    }

    String getConditionsKeyList(Collection<? extends SuperKey> keyList) {
        String conditions = "";

        for (SuperKey key : keyList)
            conditions += (!"".equals(conditions) ? " OR " : "") +
                    "(" + key.getWhereClause() + ")";

        return conditions;
    }

    String getConditionsKeyListParent(List<? extends EntityKeyWithParent> keyWithParents) {
        String conditions = "";

        for (EntityKeyWithParent keyWithParent : keyWithParents)
            conditions += (!"".equals(conditions) ? " OR " : "") +
                    "(" + keyWithParent.getWhereClauseParent() + ")";

        return conditions;
    }

    String getWhiteSpaceSepStringList(List<? extends SuperKey> keyList) {
        String whiteSpaceSepList = "";

        for (SuperKey key : keyList)
            whiteSpaceSepList += (!"".equals(whiteSpaceSepList) ? ", " : "") +
                    "'" + key.getWhiteSpaceSepValues() + "'";

        return whiteSpaceSepList;
    }

    String getGenericQuery(String whereClause) {
        return "SELECT " + getSelectColumnString() + " FROM " + getTableName() + getWhereClause(whereClause);
    }

    String getWhereClause(String whereClause) {
        return Util.getPrefixedStringOrEmptyString(whereClause, " WHERE ");
    }

    List<T> getEntityList(String whereClause) throws SQLException {
        List<T> entityList = new ArrayList<>();

        ResultSet rs = stat.executeQuery(getGenericQuery(whereClause));

        while (rs.next())
            entityList.add(getInstanceFromResultSet(rs));

        return entityList;
    }

    List<T> getEntityListFromSuperKeys(Collection<? extends SuperKey> superKeys) throws SQLException {
        List<T> entityList = new ArrayList<>();

        if (superKeys.size() > 0)
            entityList = getEntityList("(" + getConditionsKeyList(superKeys) + ")");

        return entityList;
    }

    String getUpdateSQLPart(Entity entity) {
        String[] statements = new String[getValueColumns().length];

        for (int i = 0; i < statements.length; i++)
            statements[i] = getValueColumns()[i] + " = " + entity.getPropertiesInSQLStrings()[i];

        return Util.concatStrings(statements, ", ");
    }

    String getUpdateQuery(SuperKey superKey, Entity entity, String calNowString) {
        return "UPDATE " + getTableName() + " SET " + getUpdateSQLPart(entity) +
                ", modified = " + calNowString +
                " WHERE " + superKey.getWhereClause();
    }
}
