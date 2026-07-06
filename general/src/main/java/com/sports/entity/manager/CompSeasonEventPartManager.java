package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CompSeasonEventPartManager extends SuperKeySuperManager<CompSeasonEventPartKey, CompSeasonEventPart> {
    public CompSeasonEventPartManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasoneventpart";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", compseasoneventpartid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "sportid",
                "sportdisciplineid",
                "eventpartnameid",
                "\"order\"",
                "stage",
                "\"date\"",
                "isfinal"
        };
    }

    @Override
    CompSeasonEventPart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPart();

        compSeasonEventPart.setCompetitionId(rs.getInt("competitionid"));
        compSeasonEventPart.setSeasonId(rs.getInt("seasonid"));
        compSeasonEventPart.setCompSeasonEventId(rs.getInt("compseasoneventid"));
        compSeasonEventPart.setCompSeasonEventPartId(rs.getInt("compseasoneventpartid"));
        compSeasonEventPart.setSportId(rs.getInt("sportid"));
        compSeasonEventPart.setSportDisciplineId(rs.getInt("sportdisciplineid"));
        compSeasonEventPart.setEventPartNameId(QueryUtil.getIntegerFromResultSet(rs, "eventpartnameid"));
        compSeasonEventPart.setOrder(rs.getInt("order"));
        compSeasonEventPart.setStage(QueryUtil.getIntegerFromResultSet(rs, "stage"));
        compSeasonEventPart.setDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("date")));
        compSeasonEventPart.setFinal(rs.getBoolean("isfinal"));

        return compSeasonEventPart;
    }

    @Override
    CompSeasonEventPartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonEventPartKey(((CompSeasonEventManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("compseasoneventpartid"));
    }

    @Override
    CompSeasonEventManager getSuperManager() {
        return new CompSeasonEventManager(stat);
    }

    public CompSeasonEventPart getCompSeasonEventPart(CompSeasonEventPartKey csepk) throws SQLException {
        return getEntityFromSuperKey(csepk);
    }

    public List<CompSeasonEventPartKey> getCompSeasonEventParts(CompSeasonEventKey csek) throws SQLException {
        return getSuperKeyList(csek.getWhereClause());
    }

    public Map<CompSeasonEventPartKey, CompSeasonEventPart> getCompSeasonEventPartMap(List<CompSeasonEventKey> cseKeys)
        throws SQLException {
        return getSuperKeyEntityMap(getConditionsKeyList(cseKeys));
    }

    public List<CompSeasonEventPart> getCompSeasonEventPartsFromEvents(List<CompSeasonEventKey> csekList) throws SQLException {
        return getEntityListFromSuperKeys(csekList);
    }

    public List<CompSeasonEventPart> getCompSeasonEventPartsAtStage(List<CompSeasonEventKey> cseKeys, int stage) throws SQLException {
        return getEntityList("(" + getConditionsKeyList(cseKeys) + ") AND stage = " + stage);
    }

    public List<CompSeasonEventPart> getCompSeasonEventPartsToStage(List<CompSeasonEventKey> cseKeys, int stage) throws SQLException {
        return getEntityList("(" + getConditionsKeyList(cseKeys) + ") AND stage <= " + stage);
    }

    public void insertCompSeasonEventParts(Map<CompSeasonEventPartKey, CompSeasonEventPart> csepMap) throws SQLException {
        insert(csepMap);
    }

    public int getNewCompSeasonEventPartId(CompSeasonEventKey csek) throws SQLException {
        return getNewInt("compseasoneventpartid", csek.getWhereClause());
    }
}
