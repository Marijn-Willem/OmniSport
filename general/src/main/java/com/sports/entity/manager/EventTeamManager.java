package com.sports.entity.manager;

import com.sports.entity.EventTeam;
import com.sports.entity.key.EventTeamKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EventTeamManager extends AlcifoParticipantManager<EventTeamKey, EventTeam> {
    public EventTeamManager(Statement stat) {
        super(stat);
    }

    @Override
    public String getIdColumn() {
        return "teamid";
    }

    @Override
    String getTableName() {
        return "eventteam";
    }

    @Override
    EventTeamKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventTeamKey(((CompSeasonEventManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    @Override
    EventTeam getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventTeam eventTeam = new EventTeam();

        fillGenericPropertiesFromResultSet(eventTeam, rs);

        return eventTeam;
    }
}
