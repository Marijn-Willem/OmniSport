package com.sports.entity.manager;

import com.sports.entity.EventPersonSport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.key.EventPersonSportKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventPersonSportManager extends AlcifoParticipantManager<EventPersonSportKey, EventPersonSport> {
    public EventPersonSportManager(Statement stat) {
        super(stat);
    }

    @Override
    public String getIdColumn() {
        return "personsportid";
    }

    @Override
    String getTableName() {
        return "eventpersonsport";
    }

    @Override
    EventPersonSport getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventPersonSport eventPersonSport = new EventPersonSport();

        fillGenericPropertiesFromResultSet(eventPersonSport, rs);

        return eventPersonSport;
    }

    @Override
    EventPersonSportKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventPersonSportKey(((CompSeasonEventManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    public List<Integer> getPersonSportIdsCompSeasonEvent(CompSeasonEventKey csek)
        throws SQLException {
        return getParticipantIdsInEvent(csek);
    }

    public void insertEventPersonSport(EventPersonSportKey eventPersonSportKey, EventPersonSport eventPersonSport) throws SQLException {
        insert(eventPersonSportKey, eventPersonSport);
    }

    public void insertNonExistingEventPersonSports(Map<EventPersonSportKey, EventPersonSport> eventPersonSportMap) throws SQLException {
        insertNonExistingEntries(eventPersonSportMap);
    }

    public void deleteEventPersonSports(List<EventPersonSportKey> eventPersonSportKeys) throws SQLException {
        delete(eventPersonSportKeys);
    }

    public Map<EventPersonSportKey, EventPersonSport> getEventPersonSportMap(int personSportId) throws SQLException {
        return getSuperKeyEntityMap("personsportid = " + personSportId);
    }

    public List<EventPersonSportKey> getEventsByCompSeasonsForPersonSport(List<CompSeasonPersonSportKey> compSeasonPersonSportKeys)
            throws SQLException {
        return new ArrayList<>() {{
            if (!compSeasonPersonSportKeys.isEmpty())
                addAll(getSuperKeyList(getConditionsKeyList(compSeasonPersonSportKeys)));
        }};
    }
}
