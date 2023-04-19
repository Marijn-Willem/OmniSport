package com.sports.entity.manager;

import com.sports.entity.SportEvent;
import com.sports.entity.key.SportEventKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportEventManager extends SuperKeyAliasableManager<SportEventKey, SportEvent> {
    public SportEventManager(Statement stat) {
        super(stat);
    }

    @Override
    SportEvent getInstance() {
        return new SportEvent();
    }

    @Override
    String getTableName() {
        return "sportevent";
    }

    @Override
    String getKeyColumnString() {
        return "sportid, sporteventid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "name",
                "pointssortasc",
                "genderid",
                "isteam"
            };
    }

    @Override
    SportEvent getInstanceFromResultSet(ResultSet rs) throws SQLException {
        SportEvent sportEvent = new SportEvent();

        sportEvent.setSportId(rs.getInt("sportid"));
        sportEvent.setSportEventId(rs.getInt("sporteventid"));
        sportEvent.setName(rs.getString("name"));
        sportEvent.setPointsSortAsc(rs.getBoolean("pointssortasc"));
        sportEvent.setGenderId(rs.getInt("genderid"));
        sportEvent.setTeam(rs.getBoolean("isteam"));

        return sportEvent;
    }

    @Override
    SportEventKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new SportEventKey(rs.getInt("sportid"), rs.getInt("sporteventid"));
    }

    public List<SportEvent> getSportEventListByKeys(List<SportEventKey> sportEventKeys)
        throws SQLException {
        return getEntityListFromSuperKeys(sportEventKeys);
    }

    public List<SportEvent> getSportEventList(List<Integer> sportIds) throws SQLException {
        return getEntityList("sportid IN (" + getCommaSepIntList(sportIds) + ")");
    }

    public int getNewSportEventId(int sportId) throws SQLException {
        return getNewInt("sporteventid", "sportid = " + sportId);
    }
}
