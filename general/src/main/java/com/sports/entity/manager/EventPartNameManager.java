package com.sports.entity.manager;

import com.sports.entity.EventPartName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class EventPartNameManager extends IntAliasableManager<EventPartName> {
    public EventPartNameManager(Statement stat) {
        super(stat);
    }

    @Override
    EventPartName getInstance() {
        return new EventPartName();
    }

    @Override
    String getTableName() {
        return "eventpartname";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    @Override
    EventPartName getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventPartName eventPartName = new EventPartName();

        eventPartName.setId(rs.getInt("id"));
        eventPartName.setName(rs.getString("name"));

        return eventPartName;
    }

    public List<EventPartName> getEventPartNames() throws SQLException {
        return getEntityList(null);
    }

    public Map<Integer, EventPartName> getEventPartNameMap(List<Integer> idList) throws SQLException {
        return getEntityMapFromIds(idList);
    }
}
