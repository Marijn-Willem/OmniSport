package com.sports.entity.manager;

import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.key.DisciplinePartPersonSportKey;
import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.entity.key.EventPartPersonSportKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisciplinePartPersonSportManager extends AlcifoPartParticipantManager<DisciplinePartPersonSportKey, EventDisciplinePartKey, DisciplinePartPersonSport> {
    public DisciplinePartPersonSportManager(Statement stat) {
        super(stat);
    }

    @Override
    String getParticipantIdColumn() {
        return "personsportid";
    }

    @Override
    String getTableName() {
        return "disciplinepartpersonsport";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", eventdisciplinepartid";
    }

    @Override
    public String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    DisciplinePartPersonSport getInstanceFromResultSet(ResultSet rs) throws SQLException {
        DisciplinePartPersonSport disciplinePartPersonSport = new DisciplinePartPersonSport();

        fillGenericPropertiesFromResultSet(disciplinePartPersonSport, rs);
        disciplinePartPersonSport.setEventDisciplinePartId(rs.getInt("eventdisciplinepartid"));

        return disciplinePartPersonSport;
    }

    @Override
    DisciplinePartPersonSportKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DisciplinePartPersonSportKey(
                ((EventPartPersonSportManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("eventdisciplinepartid"));
    }

    @Override
    EventPartPersonSportManager getSuperManager() {
        return new EventPartPersonSportManager(stat);
    }

    public void addDisciplinePartPerson(DisciplinePartPersonSportKey dppk, DisciplinePartPersonSport dpp)
        throws SQLException {
        super.insert(dppk, dpp);
    }

    public List<DisciplinePartPersonSport> getDisciplinePartPersonSportList(List<EventPartPersonSportKey> eventPartPersonSportKeys)
        throws SQLException {
        List<DisciplinePartPersonSport> disciplinePartPeople = new ArrayList<>();

        if (eventPartPersonSportKeys.size() > 0)
            disciplinePartPeople = getEntityList(getConditionsKeyList(eventPartPersonSportKeys));

        return disciplinePartPeople;
    }

    public Map<DisciplinePartPersonSportKey, DisciplinePartPersonSport> getDisciplinePartPersonSportMap(
            List<EventPartPersonSportKey> eventPartPersonSportKeys) throws SQLException {
        return getSuperKeyEntityMapFromSuperKeys(eventPartPersonSportKeys);
    }

    public void insertDisciplinePartPersonSports(Map<DisciplinePartPersonSportKey, DisciplinePartPersonSport> dppMap)
        throws SQLException {
        insert(dppMap);
    }

    public void deleteDisciplinePartPersonSports(List<DisciplinePartPersonSportKey> dppmKeys) throws SQLException {
        delete(dppmKeys);
    }
}
