package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventDisciplinePartKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class EventDisciplinePartManager extends SuperKeySuperManager<EventDisciplinePartKey, EventDisciplinePart> {
    public EventDisciplinePartManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "eventdisciplinepart";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", eventdisciplinepartid";
    }

    @Override
    CompSeasonEventPartManager getSuperManager() {
        return new CompSeasonEventPartManager(stat);
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "sportdisciplineid",
                "disciplinepartid",
                "name"
        };
    }

    @Override
    EventDisciplinePart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventDisciplinePart eventDisciplinePart = new EventDisciplinePart();

        eventDisciplinePart.setEventDisciplinePartId(rs.getInt("eventdisciplinepartid"));
        eventDisciplinePart.setSportDisciplineId(rs.getInt("sportdisciplineid"));
        eventDisciplinePart.setDisciplinePartId(QueryUtil.getIntegerFromResultSet(rs, "disciplinepartid"));
        eventDisciplinePart.setName(rs.getString("name"));

        return eventDisciplinePart;
    }

    @Override
    EventDisciplinePartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventDisciplinePartKey(
                ((CompSeasonEventPartManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("eventdisciplinepartid"));
    }

    public int getNewEventDisciplinePartId(CompSeasonEventPartKey csepKey) throws SQLException {
        return getNewInt("eventdisciplinepartid", csepKey.getWhereClause());
    }

    public List<EventDisciplinePart> getEventDisciplineList(CompSeasonEventPartKey csepk) throws SQLException {
        return getEntityList(csepk.getWhereClause());
    }

    public Map<EventDisciplinePartKey, EventDisciplinePart> getEventDisciplinePartMap(CompSeasonEventPartKey csepk)
        throws SQLException {
        return getSuperKeyEntityMap(csepk.getWhereClause());
    }

    public Map<EventDisciplinePartKey, EventDisciplinePart> getEventDisciplineMapFromEvents(List<CompSeasonEventKey> csekList) throws SQLException {
        return getSuperKeyEntityMapFromSuperKeys(csekList);
    }

    public void insertEventDisciplineParts(Map<EventDisciplinePartKey, EventDisciplinePart> edpMap) throws SQLException {
        insert(edpMap);
    }
}
