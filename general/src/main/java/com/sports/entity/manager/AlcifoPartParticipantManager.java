package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.key.SuperKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public abstract class AlcifoPartParticipantManager<S extends SuperKey, U extends SuperKey, T extends AlcifoPartParticipant>
        extends SuperKeySuperManager<S, T> implements AbstractSuperKeyManager {
    abstract String getParticipantIdColumn();

    public AlcifoPartParticipantManager(Statement stat) {
        super(stat);
    }

    @Override
    public String getIdColumn() {
        return null;
    }

    @Override
    public String[] getGenericColumns() {
        return new String[] {
                "points",
                "rank",
                "nocountresultid"
        };
    }

    @Override
    String[] getValueColumns() {
        return Util.concatenateStringArrays(getGenericColumns(), getSpecificValueColumns());
    }

    public List<T> getPartParticipantList(U partKey) throws SQLException {
        return getEntityList(partKey.getWhereClause());
    }

    public Map<S, T> getPartParticipantMap(List<? extends SuperKey> alcifoParticipantKeys) throws SQLException {
        return getSuperKeyEntityMapFromSuperKeys(alcifoParticipantKeys);
    }

    public void updatePartParticipantMap(Map<S, T> ppMap) throws SQLException {
        updateEntityMap(ppMap);
    }

    public void insertPartParticipantMap(Map<S, T> ppMap) throws SQLException {
        insert(ppMap);
    }

    void fillGenericPropertiesFromResultSet(T alcifoPartParticipant, ResultSet rs) throws SQLException {
        alcifoPartParticipant.setPoints(QueryUtil.getIntegerFromResultSet(rs, "points"));
        alcifoPartParticipant.setRank(QueryUtil.getIntegerFromResultSet(rs, "rank"));
        alcifoPartParticipant.setNoCountResultId(QueryUtil.getIntegerFromResultSet(rs, "nocountresultid"));
        alcifoPartParticipant.setParticipantId(rs.getInt(getParticipantIdColumn()));
    }
}
