package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.AlcifoParticipant;
import com.sports.entity.key.AlcifoParticipantKey;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public abstract class AlcifoParticipantManager<APK extends AlcifoParticipantKey, AP extends AlcifoParticipant>
    extends SuperKeySuperManager<APK, AP> implements AbstractSuperKeyManager {
    public AlcifoParticipantManager(Statement stat) {
        super(stat);
    }

    @Override
    public String[] getGenericColumns() {
        return new String[] {
                "rank",
                "nocountresultid"
        };
    }

    @Override
    public String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", " + getIdColumn();
    }

    @Override
    String[] getValueColumns() {
        return Util.concatenateStringArrays(getGenericColumns(), getSpecificValueColumns());
    }

    @Override
    CompSeasonEventManager getSuperManager() {
        return new CompSeasonEventManager(stat);
    }

    void fillGenericPropertiesFromResultSet(AP alcifoParticipant, ResultSet rs) throws SQLException {
        alcifoParticipant.setSpecificId(rs.getInt(getIdColumn()));
        alcifoParticipant.setRank(QueryUtil.getIntegerFromResultSet(rs, "rank"));
        alcifoParticipant.setNoCountResultId(QueryUtil.getIntegerFromResultSet(rs, "nocountresultid"));
    }

    public List<Integer> getParticipantIdsInEvent(CompSeasonEventKey compSeasonEventKey) throws SQLException {
        return getIdList(getGenericQuery(compSeasonEventKey.getWhereClause()), getIdColumn());
    }

    public Map<APK, AP> getParticipantMapInEvent(CompSeasonEventKey compSeasonEventKey) throws SQLException {
        return getSuperKeyEntityMap(compSeasonEventKey.getWhereClause());
    }

    public void insertParticipantMap(Map<APK, AP> participantMap) throws SQLException {
        insert(participantMap);
    }

    public void updateParticipantMap(Map<APK, AP> participantMap) throws SQLException {
        updateEntityMap(participantMap);
    }
}
