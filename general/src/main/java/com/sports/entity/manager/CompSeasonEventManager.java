package com.sports.entity.manager;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportEventKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompSeasonEventManager extends SuperKeySuperManager<CompSeasonEventKey, CompSeasonEvent> {
    public CompSeasonEventManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasonevent";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", compseasoneventid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "sportid", "sporteventid", "genderid" };
    }

    @Override
    CompSeasonEvent getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonEvent compSeasonEvent = new CompSeasonEvent();

        compSeasonEvent.setCompSeasonEventId(rs.getInt("compseasoneventid"));
        compSeasonEvent.setSportId(rs.getInt("sportid"));
        compSeasonEvent.setSportEventId(rs.getInt("sporteventid"));
        compSeasonEvent.setGenderId(rs.getInt("genderid"));

        return compSeasonEvent;
    }

    @Override
    CompSeasonManager getSuperManager() {
        return new CompSeasonManager(stat);
    }

    @Override
    CompSeasonEventKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonEventKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("compseasoneventid")
        );
    }

    public List<CompSeasonEvent> getCompSeasonEvents(CompSeasonKey csk) throws SQLException {
        return getEntityList(csk.getWhereClause());
    }

    public Map<CompSeasonEventKey, CompSeasonEvent> getCompSeasonEventMap(List<CompSeasonEventKey> cseKeys)
        throws SQLException {
        return getSuperKeyEntityMap(getConditionsKeyList(cseKeys));
    }

    public List<CompSeasonEvent> getCompSeasonEventsBySportEvents(CompSeasonKey csk, List<SportEventKey> seKeys)
        throws SQLException {
        List<CompSeasonEvent> compSeasonEventKeys = new ArrayList<>();

        if (!seKeys.isEmpty())
            compSeasonEventKeys.addAll(getEntityList(
                    csk.getWhereClause() + " AND (" + getConditionsKeyList(seKeys) + ")"));

        return compSeasonEventKeys;
    }

    public void insertCompSeasonEventMap(Map<CompSeasonEventKey, CompSeasonEvent> cseMap) throws SQLException {
        insert(cseMap);
    }

    public CompSeasonEventKey getNewCompSeasonEventKey(CompSeasonKey compSeasonKey) throws SQLException {
        int compSeasonEventId = getNewInt("compseasoneventid", compSeasonKey.getWhereClause());
        return new CompSeasonEventKey(compSeasonKey, compSeasonEventId);
    }
}
