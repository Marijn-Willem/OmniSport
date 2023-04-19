package com.sports.entity.manager;

import com.sports.entity.PersonMatch;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.PersonMatchKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class PersonMatchManager extends H2HMatchManager<PersonMatchKey, PersonMatch> {
    public PersonMatchManager(Statement stat) {
        super(stat);
    }

    public String[] getSpecificValueColumns() {
        return new String[] {
                "person1start"
        };
    }

    protected String getParticipant1IdColumn() {
        return "personsport1id";
    }

    protected String getParticipant2IdColumn() {
        return "personsport2id";
    }

    protected String getScore1Column() {
        return "person1score";
    }

    protected String getScore2Column() {
        return "person2score";
    }

    @Override
    protected String getParticipant1NcrIdColumn() {
        return "person1ncrid";
    }

    @Override
    protected String getParticipant2NcrIdColumn() {
        return "person2ncrid";
    }

    public PersonMatch getPersonMatch(PersonMatchKey pmk)
        throws SQLException {
        return getInstanceFromKey(pmk);
    }

    public List<PersonMatch> getPersonMatchList(CompSeasonKey csk, String whereClause)
            throws SQLException {
        return getH2HMatchList(csk, whereClause);
    }

    public List<PersonMatch> getPersonMatchesFromCompSeasonPhases(List<CompSeasonPhaseKey> phaseKeys)
        throws SQLException {
        return getH2HMatchesFromCompSeasonPhases(phaseKeys);
    }

    public void updatePersonMatch(PersonMatchKey pmk, PersonMatch pm) throws SQLException {
        update(pmk, pm);
    }

    public List<PersonMatch> getPersonMatchList(CompSeasonKey csk) throws SQLException {
        return getH2HMatchList(csk, null);
    }

    public void insertPersonMatch(PersonMatchKey pmk, PersonMatch pm)
        throws SQLException {
        insert(pmk, pm);
    }

    public void insertPersonMatchMap(Map<PersonMatchKey, PersonMatch> pmMap) throws SQLException {
        insert(pmMap);
    }

    protected String getTableName() {
        return "personmatch";
    }

    public String getIdColumn() {
        return "personmatchid";
    }

    protected PersonMatch getInstanceFromResultSet(ResultSet rs) throws SQLException {
        PersonMatch personMatch = new PersonMatch();

        fillGenericPropertiesFromResultSet(personMatch, rs);
        personMatch.setPersonMatchId(rs.getInt("personmatchid"));
        personMatch.setPerson1Start(rs.getBoolean("person1start"));

        return personMatch;
    }

    @Override
    PersonMatchKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new PersonMatchKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }
}
