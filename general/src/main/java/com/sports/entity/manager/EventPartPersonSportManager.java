package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPartPersonSportKey;
import com.sports.entity.key.EventPersonSportKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventPartPersonSportManager extends AlcifoPartParticipantManager<EventPartPersonSportKey, CompSeasonEventPartKey, EventPartPersonSport> {
    public EventPartPersonSportManager(Statement stat) {
        super(stat);
    }

    @Override
    String getParticipantIdColumn() {
        return "personsportid";
    }

    @Override
    String getTableName() {
        return "eventpartpersonsport";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", personsportid";
    }

    @Override
    public String[] getSpecificValueColumns() {
        return new String[] {
                "heat"
        };
    }

    @Override
    CompSeasonEventPartManager getSuperManager() {
        return new CompSeasonEventPartManager(stat);
    }

    @Override
    EventPartPersonSport getInstanceFromResultSet(ResultSet rs) throws SQLException {
        EventPartPersonSport eventPartPersonSport = new EventPartPersonSport();

        fillGenericPropertiesFromResultSet(eventPartPersonSport, rs);
        eventPartPersonSport.setCompSeasonEventPartId(rs.getInt("compseasoneventpartid"));
        eventPartPersonSport.setHeat(QueryUtil.getIntegerFromResultSet(rs, "heat"));

        return eventPartPersonSport;
    }

    @Override
    EventPartPersonSportKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new EventPartPersonSportKey(((CompSeasonEventPartManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("personsportid"));
    }

    public List<Integer> getPersonSportIdsCompSeasonEventPartNoHeat(CompSeasonEventPartKey csepk)
        throws SQLException {
        return getPersonSportIdsCompSeasonEventPart(csepk, "heat IS NULL");
    }

    public List<Integer> getPersonSportIdsCompSeasonEventPart(CompSeasonEventPartKey csepk)
        throws SQLException {
        return getPersonSportIdsCompSeasonEventPart(csepk, null);
    }

    public int getNewHeat(CompSeasonEventPartKey csepk) throws SQLException {
        return getNewInt("heat", csepk.getWhereClause());
    }

    public void updateEventPartPersonSport(EventPartPersonSportKey eppk, EventPartPersonSport epp)
        throws SQLException {
        super.update(eppk, epp);
    }

    public List<EventPartPersonSport> getEventPartPersonSportListWithHeats(CompSeasonEventKey csek)
            throws SQLException {
        return getEntityList(csek.getWhereClause() + " AND heat IS NOT NULL");
    }

    public void insertEventPartPersonSport(EventPartPersonSportKey eppk, EventPartPersonSport epp)
        throws SQLException {
        insert(eppk, epp);
    }

    public void insertEventPartPersonSportMap(Map<EventPartPersonSportKey, EventPartPersonSport> eppsMap) throws SQLException {
        insert(eppsMap);
    }

    public void deleteEventPartPersonSports(List<EventPartPersonSportKey> keyList) throws SQLException {
        delete(keyList);
    }

    public List<EventPartPersonSport> getEventPartPersonSportList(List<EventPartPersonSportKey> eventPartPersonSportKeys,
                                                                  String whereClause) throws SQLException {
        List<EventPartPersonSport> eventPartPersonSportList = new ArrayList<>();
        
        if (eventPartPersonSportKeys.size() > 0) {
            eventPartPersonSportList = getEntityList(
                    Util.concatStringsWithDelimiter("(" + getConditionsKeyList(eventPartPersonSportKeys) + ")",
                            whereClause, " AND ")
            );
        }
        
        return eventPartPersonSportList;
    }

    public List<EventPartPersonSport> getEventPartPersonSportList(CompSeasonEventPartKey csepk,
                                                                  String whereClause) throws SQLException {
        return getEntityList(Util.concatStringsWithDelimiter(csepk.getWhereClause(), whereClause, " AND "));
    }

    public Map<EventPartPersonSportKey, EventPartPersonSport> getEventPartPersonSportMap(List<EventPersonSportKey> eventPersonSportKeys)
        throws SQLException {
        return getPartParticipantMap(eventPersonSportKeys);
    }

    public Map<EventPartPersonSportKey, EventPartPersonSport> getEventPartPersonSportMapForEvents(
            List<CompSeasonEventPartKey> eventPartKeys) throws SQLException {
        return getPartParticipantMap(eventPartKeys);
    }

    public Map<EventPartPersonSportKey, EventPartPersonSport> getEventPartPersonSportsWithNoCountResult(CompSeasonEventKey eventKey)
        throws SQLException {
        return getSuperKeyEntityMap(eventKey.getWhereClause() + " AND nocountresultid IS NOT NULL");
    }

    private List<Integer> getPersonSportIdsCompSeasonEventPart(CompSeasonEventPartKey csepk,
                                                               String whereClauseSuppl) throws SQLException {
        return getIdList(getGenericQuery(
                Util.concatStringsWithDelimiter(csepk.getWhereClause(), whereClauseSuppl, " AND ")),
                "personsportid");
    }
}
