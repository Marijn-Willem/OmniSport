package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.PersonMatchPart;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PersonMatchPartManager extends H2HMatchPartManager<PersonMatchPartKey, PersonMatchPart> {
    public PersonMatchPartManager(Statement stat) {
        super(stat);
    }

    public String getIdColumn() {
        return "personmatchpartid";
    }

    public String[] getSpecificValueColumns() {
        return new String[] { "person1win" };
    }

    public void insertPersonMatchPart(PersonMatchPartKey pmpk, PersonMatchPart pmp)
            throws SQLException {
        insert(pmpk, pmp);
    }

    public void updatePersonMatchPart(PersonMatchPartKey pmpk, PersonMatchPart pmp)
        throws SQLException {
        update(pmpk, pmp);
    }

    public PersonMatchPart getPersonMatchPart(PersonMatchPartKey pmpk) throws SQLException {
        return getH2HMatchPart(pmpk);
    }

    public List<PersonMatchPart> getPersonMatchPartsWithoutParent(PersonMatchKey pmk)
            throws SQLException {
        return getH2HMatchPartsWithoutParent(pmk);
    }

    public List<PersonMatchPart> getPersonMatchPartsFromParents(List<PersonMatchPartKey> personMatchPartKeys)
            throws SQLException {
        return getH2HMatchPartsFromParents(personMatchPartKeys);
    }

    @Override
    protected String getTableName() {
        return "personmatchpart";
    }

    @Override
    protected PersonMatchPart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        PersonMatchPart personMatchPart = new PersonMatchPart();

        personMatchPart.setPersonMatchPartId(rs.getInt("personmatchpartid"));
        personMatchPart.setName(rs.getString("name"));
        personMatchPart.setParentMatchPartId(QueryUtil.getIntegerFromResultSet(rs, "parentmatchpartid"));
        personMatchPart.setPerson1Win(rs.getBoolean("person1win"));
        personMatchPart.setFinished(rs.getBoolean("finished"));

        return personMatchPart;
    }

    protected PersonMatchManager getSuperManager() {
        return new PersonMatchManager(stat);
    }

    @Override
    PersonMatchPartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new PersonMatchPartKey(((PersonMatchManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }
}
