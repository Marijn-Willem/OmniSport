package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonParticipantKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CompSeasonParticipantManager<S extends CompSeasonParticipantKey, T extends SuperKeyEntity>
        extends SuperKeySuperManager<S, T> implements AbstractSuperKeyManager {
    CompSeasonParticipantManager(Statement stat) {
        super(stat);
    }

    public String[] getGenericColumns() {
        return new String[0];
    }

    public String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", " + getIdColumn();
    }

    @Override
    String[] getValueColumns() {
        return new String[0];
    }

    @Override
    T getInstanceFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    CompSeasonManager getSuperManager() {
        return new CompSeasonManager(stat);
    }

    public List<Integer> getParticipantIdsCompSeason(CompSeasonKey csk) throws SQLException {
        return getIdList(getGenericQuery(csk.getWhereClause()), getIdColumn());
    }

    public List<S> getCompSeasonsFromParticipant(int particId)
        throws SQLException {
        return getSuperKeyList(getIdColumn() + " = " + particId);
    }

    public Set<CompSeasonKey> getCompSeasonsForParticipants(List<Integer> participantIds) throws SQLException {
        return new HashSet<>() {{
            if (!participantIds.isEmpty()) {
                List<S> cspKeys = getSuperKeyList(getIdColumn() + " IN (" +
                        getCommaSepIntList(participantIds) + ")");
                addAll(cspKeys.stream().map(S::getSuperKey).collect(Collectors.toSet()));
            }
        }};
    }

    public void insertCompSeasonParticipants(List<S> keys) throws SQLException {
        insert(keys);
    }

    public void deleteCompSeasonParticipants(List<S> keys) throws SQLException {
        delete(keys);
    }
}
