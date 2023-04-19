package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Alias;
import com.sports.entity.key.AliasEntityIdKey;
import com.sports.entity.key.AliasKey;
import com.sports.entity.key.SuperKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AliasManager extends SuperKeySuperManager<AliasKey, Alias> {
    public AliasManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "alias";
    }

    @Override
    String getKeyColumnString() {
        return "aliasentityid, aliasid";
    }

    @Override
    String[] getValueColumns() {
        return new String[]{ "entityid", "languageid", "clientid", "alias" };
    }

    @Override
    Alias getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Alias alias = new Alias();

        alias.setAliasEntityId(rs.getInt("aliasentityid"));
        alias.setAliasId(rs.getInt("aliasid"));
        alias.setEntityId(rs.getString("entityid"));
        alias.setLanguageId(QueryUtil.getIntegerFromResultSet(rs, "languageid"));
        alias.setClientId(QueryUtil.getIntegerFromResultSet(rs, "clientid"));
        alias.setAlias(rs.getString("alias"));

        return alias;
    }

    @Override
    AliasKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new AliasKey(rs.getInt("aliasentityid"), rs.getInt("aliasid"));
    }

    public Alias getAlias(AliasKey aliasKey) throws SQLException {
        return getEntity(aliasKey.getWhereClause());
    }

    public void updateAliases(Map<AliasKey, Alias> aliasMap) throws SQLException {
        updateEntityMap(aliasMap);
    }

    public void insertAliases(Map<AliasKey, Alias> aliasMap) throws SQLException {
        insert(aliasMap);
    }

    public List<Alias> getAliasListFromEntityId(int aliasEntityId, int entityId) throws SQLException {
        String whereClause = "aliasentityid = " + aliasEntityId + " AND entityid = '" + entityId + "'";

        return getEntityList(whereClause);
    }

    public List<Alias> getAliasListFromEntityId(int aliasEntityId, SuperKey entityId) throws SQLException {
        String whereClause = "aliasentityid = " + aliasEntityId +
                " AND entityid = '" + entityId.getWhiteSpaceSepValues() + "'";

        return getEntityList(whereClause);
    }

    public List<Alias> getAliasListFromEntityIdLang(int aliasEntityId, String entityId)
            throws SQLException {
        String whereClause = "aliasentityid = " + aliasEntityId +
                " AND entityid = '" + entityId + "' AND languageid IS NOT NULL";

        return getEntityList(whereClause);
    }

    public List<Alias> getAliasListFromEntityIdsLang(int aliasEntityId, List<Integer> entityIds,
                                                     int languageId) throws SQLException {
        List<Alias> aliasList = new ArrayList<Alias>();

        if (entityIds.size() > 0) {
            List<String> entityIdsStr = new ArrayList<String>();

            for (Integer entityId : entityIds)
                entityIdsStr.add(entityId.toString());

            String whereClause = "aliasentityid = " + aliasEntityId +
                    " AND entityid IN (" + getCommaSepStringList(entityIdsStr) + ") " +
                    "AND languageid = " + languageId;

            aliasList = getEntityList(whereClause);
        }

        return aliasList;
    }

    public List<Alias> getAliasListFromEntityIdsLangSupKey(int aliasEntityId, List<? extends SuperKey> entityIds,
                                                           int languageId) throws SQLException {
        List<Alias> aliasList = new ArrayList<Alias>();

        if (entityIds.size() > 0) {
            String whereClause = "aliasentityid = " + aliasEntityId +
                    " AND entityid IN (" + getWhiteSpaceSepStringList(entityIds) + ") " +
                    "AND languageid = " + languageId;

            aliasList = getEntityList(whereClause);
        }

        return aliasList;
    }

    public List<Alias> getAliasListFromEntityId(AliasEntityIdKey key) throws SQLException {
        return getEntityList(key.getWhereClause());
    }

    public List<Alias> getAliasListFromEntityIds(List<AliasEntityIdKey> keys) throws SQLException {
        return getEntityListFromSuperKeys(keys);
    }

    public List<Alias> getClientAliasList() throws SQLException {
        return getEntityList("clientid IS NOT NULL");
    }

    public List<Alias> getAliasListClient(int clientId) throws SQLException {
        return getEntityList("clientid = " + clientId);
    }

    public List<Alias> getAliasesByEntitiesClientLang(List<AliasEntityIdKey> keys, Integer clientId, Integer languageId)
        throws SQLException {
        return new ArrayList<Alias>() {{
            if (keys.size() > 0) {
                String whereClause = "(" + getConditionsKeyList(keys) + ")" +
                        Util.getPrefixedStringOrEmptyString(Util.convertIntegerToString(clientId), " AND clientid = ") +
                        Util.getPrefixedStringOrEmptyString(Util.convertIntegerToString(languageId), " AND languageid = ");

                addAll(getEntityList(whereClause));
            }
        }};
    }

    public int getNewAliasId(int aliasEntityId) throws SQLException {
        return getNewInt("aliasid", "aliasentityid = " + aliasEntityId);
    }

    public void deleteAlias(AliasKey aliasKey) throws SQLException {
        delete(Collections.singletonList(aliasKey));
    }
}
