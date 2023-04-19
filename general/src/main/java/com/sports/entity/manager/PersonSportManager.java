package com.sports.entity.manager;

import com.sports.entity.Double;
import com.sports.entity.Person;
import com.sports.entity.PersonSport;
import com.sports.entity.key.DoublePersonSport1PersonSport2Key;
import com.sports.entity.key.PersonSportIdKey;
import com.sports.logic.calculation.Calculation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonSportManager extends ParticipantManager<PersonSport> {
    public PersonSportManager(Statement stat) {
        super(stat);
    }

    public List<PersonSport> getPersonSportsFromPersonSportIds(List<PersonSportIdKey> keys) throws SQLException {
        return getEntityListFromSuperKeys(keys);
    }

    public List<PersonSport> getPersonSportsForPerson(int personId) throws SQLException {
        return getEntityList("personid = " + personId);
    }

    public void insertPersonSports(List<PersonSport> personSports, int idStart) throws SQLException {
        insertIdEntities(personSports, idStart);
    }

    public void deletePersonSportsForPerson(int personId) throws SQLException {
        delete("personid = " + personId);
    }

    public void updatePersonSports(Map<Integer, PersonSport> personSportMap) throws SQLException {
        update(personSportMap);
    }

    public Map<Integer, PersonSport> getPersonSportMap(List<Integer> personSportIds) throws SQLException {
        return getEntityMapFromIds(personSportIds);
    }

    String[] getSpecificValueColumns() {
        return new String[] {
                "personid",
                "sportid"
        };
    }

    String getTableName() {
        return "personsport";
    }

    PersonSport getInstanceFromResultSet(ResultSet rs) throws SQLException {
        PersonSport personSport = new PersonSport();

        fillGeneralPropertiesFromResultSet(rs, personSport);
        personSport.setPersonId(rs.getInt("personid"));
        personSport.setSportId(rs.getInt("sportid"));

        return personSport;
    }

    @Override
    void processAfterUpdate(int personSportId) throws SQLException {
        DoubleManager dm = new DoubleManager(stat);

        Map<DoublePersonSport1PersonSport2Key, com.sports.entity.Double> map = dm.getDoublesWithPersonSport(personSportId);

        List<Double> doubleList = new ArrayList<>();
        List<Integer> personSportIds = new ArrayList<>();
        for (Map.Entry<DoublePersonSport1PersonSport2Key, com.sports.entity.Double> me : map.entrySet()) {
            doubleList.add(me.getValue());
            personSportIds.add(me.getValue().getPersonSport1Id());
            personSportIds.add(me.getValue().getPersonSport2Id());
        }

        Map<Integer, PersonSport> personSportMap = getPersonSportMap(personSportIds);

        List<Integer> personIds = personSportMap.values().stream().map(PersonSport::getPersonId)
                .collect(Collectors.toList());
        Map<Integer, Person> personMap = new PersonManager(stat).getPersonMap(personIds);
        Calculation.setGenderIdsOnPersonSports(personSportMap.values(), personMap);

        for (Double dbl : doubleList)
            Calculation.setPersonSportIdsOnDouble(dbl, dbl.getPersonSport1Id(), dbl.getPersonSport2Id(), personSportMap);

        Calculation.setDoubleDescriptions(doubleList, personSportMap);

        dm.updateDoubleMap(map);
    }
}
