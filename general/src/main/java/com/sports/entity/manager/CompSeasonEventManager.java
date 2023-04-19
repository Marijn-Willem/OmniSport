package com.sports.entity.manager;

import com.sports.entity.CompSeasonEvent;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        return getCachedSuperManager().getKeyColumnString() + ", sportid, sporteventid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "externalsource" };
    }

    @Override
    CompSeasonEvent getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonEvent compSeasonEvent = new CompSeasonEvent();

        compSeasonEvent.setExternalSource(rs.getString("externalsource"));

        return compSeasonEvent;
    }

    @Override
    CompSeasonManager getSuperManager() {
        return new CompSeasonManager(stat);
    }

    @Override
    CompSeasonEventKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonEventKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("sportid"), rs.getInt("sporteventid")
        );
    }

    public List<CompSeasonEventKey> getCompSeasonEventKeys(CompSeasonKey csk) throws SQLException {
        return getSuperKeyList(csk.getWhereClause());
    }

    public void insertCompSeasonEvent(CompSeasonEventKey csek, CompSeasonEvent cse) throws SQLException {
        insert(csek, cse);
    }

    public void insertCompSeasonEventMap(Map<CompSeasonEventKey, CompSeasonEvent> cseMap) throws SQLException {
        insert(cseMap);
    }
}
