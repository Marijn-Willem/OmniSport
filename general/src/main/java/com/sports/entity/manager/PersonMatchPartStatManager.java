package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.PersonMatchPartStat;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.key.PersonMatchPartStatKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class PersonMatchPartStatManager extends H2HMatchPartStatManager<PersonMatchPartStatKey, PersonMatchPartStat> {
    public PersonMatchPartStatManager(Statement stat) {
        super(stat);
    }

    public String[] getSpecificValueColumns() {
        return new String[] { "personsportid", "value2" };
    }

    public void insertPersonMatchPartStat(PersonMatchPartStatKey pmpsk, PersonMatchPartStat pmps)
        throws SQLException {
        insert(pmpsk, pmps);
    }

    public void updatePersonMatchPartStat(PersonMatchPartStatKey pmpsk, PersonMatchPartStat pmps)
        throws SQLException {
        update(pmpsk, pmps);
    }

    public List<PersonMatchPartStat> getPersonMatchPartStats(List<PersonMatchPartKey> personMatchPartKeys,
                                                             List<Integer> statTypeIds)
            throws SQLException {
        return getH2HMatchPartStats(personMatchPartKeys, statTypeIds);
    }

    public Map<PersonMatchPartStatKey, PersonMatchPartStat> getPersonMatchPartStatMap(
            List<CompSeasonPersonSportKey> compSeasonPersonSportKeys) throws SQLException {
        return getH2HMatchPartStatMap(compSeasonPersonSportKeys);
    }

    protected String getTableName() {
        return "personmatchpartstat";
    }

    public String getIdColumn() {
        return "personmatchpartstatid";
    }

    @Override
    protected PersonMatchPartStat getInstanceFromResultSet(ResultSet rs) throws SQLException {
        PersonMatchPartStat personMatchPartStat = new PersonMatchPartStat();

        fillGenericPropertiesFromResultSet(rs, personMatchPartStat);
        personMatchPartStat.setPersonMatchPartId(rs.getInt("personmatchpartid"));
        personMatchPartStat.setPersonMatchPartStatId(rs.getInt("personmatchpartstatid"));
        personMatchPartStat.setPersonSportId(rs.getInt("personsportid"));
        personMatchPartStat.setValue2(QueryUtil.getIntegerFromResultSet(rs, "value2"));

        return personMatchPartStat;
    }

    protected PersonMatchPartManager getSuperManager() {
        return new PersonMatchPartManager(stat);
    }

    @Override
    PersonMatchPartStatKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new PersonMatchPartStatKey(((PersonMatchPartManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }
}
