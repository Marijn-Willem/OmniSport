package com.sports.entity.manager;

import com.sports.entity.EventPartName;
import com.sports.entity.key.EventPartNameKey;
import com.sports.entity.key.SportEventKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class EventPartNameManager extends SuperKeyAliasableManager<EventPartNameKey, EventPartName> {
    public EventPartNameManager(Statement stat) {
        super(stat);
    }

    @Override
    EventPartName getInstance() {
        return new EventPartName();
    }

    @Override
    SportEventManager getSuperManager() {
        return new SportEventManager(stat);
    }

    @Override
    EventPartNameKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventPartNameKey(
                ((SportEventManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("eventpartnameid")
        );
    }

    @Override
    String getTableName() {
        return "eventpartname";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", eventpartnameid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    @Override
    EventPartName getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventPartName eventPartName = new EventPartName();

        eventPartName.setEventPartNameId(rs.getInt("eventpartnameid"));
        eventPartName.setName(rs.getString("name"));

        return eventPartName;
    }

    public int getNewEventPartNameId(SportEventKey sportEventKey) throws SQLException {
        return getNewInt("eventpartnameid", sportEventKey.getWhereClause());
    }

    public List<EventPartName> getEventPartNames(SportEventKey sportEventKey) throws SQLException {
        return getEntityList(sportEventKey.getWhereClause());
    }

    public Map<EventPartNameKey, EventPartName> getEventPartNameMap(List<EventPartNameKey> eventPartNameKeys)
        throws SQLException {
        return getSuperKeyEntityMap(getConditionsKeyList(eventPartNameKeys));
    }
}
