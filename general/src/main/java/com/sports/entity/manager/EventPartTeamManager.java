package com.sports.entity.manager;

import com.sports.entity.EventPartTeam;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartTeamKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EventPartTeamManager extends AlcifoPartParticipantManager<EventPartTeamKey, CompSeasonEventPartKey, EventPartTeam> {
    public EventPartTeamManager(Statement stat) {
        super(stat);
    }

    @Override
    String getParticipantIdColumn() {
        return "teamid";
    }

    @Override
    public String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    EventPartTeamKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventPartTeamKey(((CompSeasonEventPartManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("teamid"));
    }

    @Override
    String getTableName() {
        return "eventpartteam";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", teamid";
    }

    @Override
    EventPartTeam getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventPartTeam eventPartTeam = new EventPartTeam();

        fillGenericPropertiesFromResultSet(eventPartTeam, rs);

        return eventPartTeam;
    }

    @Override
    CompSeasonEventPartManager getSuperManager() {
        return new CompSeasonEventPartManager(stat);
    }
}
