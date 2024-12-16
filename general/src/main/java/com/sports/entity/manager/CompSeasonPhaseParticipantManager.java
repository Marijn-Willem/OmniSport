package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseParticipantKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CompSeasonPhaseParticipantManager<R extends CompSeasonParticipantKey,
        S extends CompSeasonPhaseParticipantKey>
        extends SuperKeySuperManager<S, SuperKeyEntity> {
    abstract String getSpecificIdColumn();

    CompSeasonPhaseParticipantManager(Statement stat) {
        super(stat);
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", " + getSpecificIdColumn();
    }

    @Override
    String[] getValueColumns() {
        return new String[0];
    }

    @Override
    CompSeasonPhaseManager getSuperManager() {
        return new CompSeasonPhaseManager(stat);
    }

    @Override
    SuperKeyEntity getInstanceFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    public List<Integer> getParticipantIds(CompSeasonPhaseKey compSeasonPhaseKey) throws SQLException {
        return getParticipantsInCompSeasonPhases(Collections.singletonList(compSeasonPhaseKey));
    }

    public List<Integer> getParticipantsInCompSeasonPhases(List<CompSeasonPhaseKey> compSeasonPhaseKeys)
        throws SQLException {
        String query = "SELECT " + getSpecificIdColumn() + " FROM " + getTableName() + " WHERE (" +
                getConditionsKeyList(compSeasonPhaseKeys) + ")";

        return getIdList(query, getSpecificIdColumn());
    }

    public void insert(CompSeasonPhaseParticipantKey key) throws SQLException {
        super.insert(key);
    }

    public void insertPhaseParticipantKeyList(List<? extends CompSeasonPhaseParticipantKey> keys) throws SQLException {
        insert(keys);
    }

    public void deletePhaseParticipants(List<S> keys) throws SQLException {
        delete(keys);
    }

    public List<S> getPhaseParticipants(List<R> cspKeys) throws SQLException {
        List<S> phaseParticipants = new ArrayList<>();

        if (!cspKeys.isEmpty())
            phaseParticipants = getSuperKeyList(getConditionsKeyList(cspKeys));

        return phaseParticipants;
    }
}
