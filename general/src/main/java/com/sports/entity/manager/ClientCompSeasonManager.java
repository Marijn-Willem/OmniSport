package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.ClientCompSeasonKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ClientCompSeasonManager extends SuperKeySuperManager<ClientCompSeasonKey, SuperKeyEntity> {
    public ClientCompSeasonManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "clientcompseason";
    }

    @Override
    String getKeyColumnString() {
        return "clientid, competitionid, seasonid";
    }

    @Override
    String[] getValueColumns() {
        return new String[0];
    }

    @Override
    SuperKeyEntity getInstanceFromResultSet(ResultSet rs) {
        return null;
    }

    @Override
    ClientCompSeasonKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new ClientCompSeasonKey(rs.getInt("clientid"),
                rs.getInt("competitionid"),
                rs.getInt("seasonid"));
    }

    public Set<CompSeasonKey> getCompSeasonKeys(int clientId) throws SQLException {
        return new HashSet<>() {{
            getSuperKeyList("clientid = " + clientId).forEach(x ->
                    add(new CompSeasonKey(x.getCompetitionId(), x.getSeasonId()))
            );
        }};
    }

    public List<Integer> getClientIdsForCompSeason(CompSeasonKey compSeasonKey) throws SQLException {
        return getIdList(getGenericQuery(compSeasonKey.getWhereClause()), "clientid");
    }

    public List<Integer> getClientIdsForCompetitions(List<Integer> competitionIds) throws SQLException {
        List<Integer> clientIds = new ArrayList<>();

        if (!competitionIds.isEmpty()) {
            String whereClause = "competitionid IN (" + getCommaSepIntList(competitionIds) + ")";
            Set<Integer> idSet = new HashSet<>(getIdList(getGenericQuery(whereClause), "clientid"));
            clientIds.addAll(idSet);
        }

        return clientIds;
    }

    public Map<CompSeasonKey, List<Integer>> getCompSeasonClientIdMap(Collection<CompSeasonKey> compSeasonKeys)
            throws SQLException {
        List<ClientCompSeasonKey> ccsKeys = getSuperKeyList(getConditionsKeyList(compSeasonKeys));

        return new HashMap<>() {{
            ccsKeys.forEach(x -> {
                CompSeasonKey csKey = x.getCompSeasonKey();
                if (!containsKey(csKey))
                    put(csKey, new ArrayList<>());

                get(csKey).add(x.getClientId());
            });
        }};
    }

    public void insertClientCompSeasons(List<ClientCompSeasonKey> keys) throws SQLException {
        insert(keys);
    }

    public void deleteClientCompSeasons(List<ClientCompSeasonKey> keys) throws SQLException {
        delete(keys);
    }
}
