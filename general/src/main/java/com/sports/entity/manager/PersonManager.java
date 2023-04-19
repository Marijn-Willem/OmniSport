package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Entity;
import com.sports.entity.Person;
import com.sports.entity.PersonInstance;
import com.sports.entity.PersonSport;
import com.sports.entity.key.PersonInstanceKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonManager extends InstanceEntityManager<Person, PersonInstanceKey, PersonInstance> {
    public PersonManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "person";
    }

    String[] getValueColumns() {
        return new String[] {
                "name",
                "genderid",
                "geoid"
        };
    }

    @Override
    Person getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Person person = new Person();

        person.setId(rs.getInt("id"));
        person.setName(rs.getString("name"));
        person.setGenderId(rs.getInt("genderid"));
        person.setGeoId(QueryUtil.getIntegerFromResultSet(rs, "geoid"));

        return person;
    }

    @Override
    EntityInstanceManager<PersonInstanceKey, PersonInstance> getEntityInstanceManager() {
        return new PersonInstanceManager(stat);
    }

    @Override
    PersonInstance getEntityInstance() {
        return new PersonInstance();
    }

    @Override
    PersonInstanceKey getEntityInstanceKey(PersonInstance entityInstance) {
        return new PersonInstanceKey(entityInstance.getEntityId(), entityInstance.getEntityInstanceId());
    }

    public Map<String, Integer> getNamePersonIdMap(List<String> names) throws SQLException {
        Map<String, Integer> map = new HashMap<>();

        if (!names.isEmpty()) {
            String query = getGenericQuery("name IN (" +
                    getCommaSepStringList(names) + ")");

            ResultSet rs = stat.executeQuery(query);

            while (rs.next())
                map.put(rs.getString("name"), rs.getInt("id"));
        }

        return map;
    }

    public Map<String, Person> getNamePersonMap(List<String> names) throws SQLException {
        Map<String, Person> map = new HashMap<>();

        if (!names.isEmpty()) {
            ResultSet rs = stat.executeQuery(getGenericQuery("name IN (" +
                    getCommaSepStringList(names) + ")"));

            while (rs.next())
                map.put(rs.getString("name"), getInstanceFromResultSet(rs));
        }

        return map;
    }

    public Map<Integer, Person> getPersonMap(List<Integer> ids) throws SQLException {
        return getEntityMapFromIds(ids);
    }

    public List<Person> getPersonList(List<Integer> ids) throws SQLException {
        return getEntityListFromIds(ids);
    }

    public List<Person> getPersonListNameLike(String name) throws SQLException {
        return getEntityListNameLike(name);
    }

    public int getNewPersonId() throws SQLException {
        return getNewId();
    }

    public void insertPersons(List<Person> personList, int personIdStart) throws SQLException {
        insertIdEntities(personList, personIdStart);
    }

    @Override
    public void update(int id, Entity entity) throws SQLException {
        super.update(id, entity);
    }

    public void deletePerson(int personId) throws SQLException {
        delete(personId);
    }

    public Person getPersonByName(String name) throws SQLException {
        Map<String, Person> map = getNamePersonMap(Collections.singletonList(name));

        if (map.containsKey(name))
            return map.get(name);

        return null;
    }

    @Override
    void processAfterUpdate(int personId) throws SQLException {
        String personName = getEntityFromId(personId).getName();

        Map<Integer, PersonSport> personSportMap = new HashMap<>();

        PersonSportManager psm = new PersonSportManager(stat);
        List<PersonSport> personSportList = psm.getPersonSportsForPerson(personId);

        for (PersonSport personSport : personSportList) {
            personSport.setDescription(personName);
            personSportMap.put(personSport.getId(), personSport);
        }

        psm.updateParticipantMap(personSportMap);
    }
}
