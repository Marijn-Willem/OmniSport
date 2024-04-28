package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.SuperKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public abstract class AlcifoPartParticipantManager<APPK extends SuperKey, PK extends SuperKey, APP extends AlcifoPartParticipant>
        extends SuperKeySuperManager<APPK, APP> implements AbstractSuperKeyManager {
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

    public List<APP> getPartParticipantList(PK partKey) throws SQLException {
        return getEntityList(partKey.getWhereClause());
    }

    public Map<APPK, APP> getPartParticipantMap(List<? extends SuperKey> alcifoParticipantKeys) throws SQLException {
        return getSuperKeyEntityMapFromSuperKeys(alcifoParticipantKeys);
    }

    public List<APP> getPartParticipantsWithRankOne(CompSeasonEventKey compSeasonEventKey) throws SQLException {
        return getEntityList(compSeasonEventKey.getWhereClause() + " AND rank = 1");
    }

    public void updatePartParticipantMap(Map<APPK, APP> ppMap) throws SQLException {
        updateEntityMap(ppMap);
    }

    public void insertPartParticipantMap(Map<APPK, APP> ppMap) throws SQLException {
        insert(ppMap);
    }

    void fillGenericPropertiesFromResultSet(APP alcifoPartParticipant, ResultSet rs) throws SQLException {
        alcifoPartParticipant.setCompSeasonEventPartId(rs.getInt("compseasoneventpartid"));
        alcifoPartParticipant.setPoints(QueryUtil.getIntegerFromResultSet(rs, "points"));
        alcifoPartParticipant.setRank(QueryUtil.getIntegerFromResultSet(rs, "rank"));
        alcifoPartParticipant.setNoCountResultId(QueryUtil.getIntegerFromResultSet(rs, "nocountresultid"));
        alcifoPartParticipant.setParticipantId(rs.getInt(getParticipantIdColumn()));
    }
}
