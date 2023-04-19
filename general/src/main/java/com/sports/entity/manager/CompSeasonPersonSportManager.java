package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonPersonSportKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

public class CompSeasonPersonSportManager extends CompSeasonParticipantManager<CompSeasonPersonSportKey, SuperKeyEntity> {
    public CompSeasonPersonSportManager(Statement stat) {
        super(stat);
    }

    public String getIdColumn() {
        return "personsportid";
    }

    CompSeasonPersonSportKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPersonSportKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    String getTableName() {
        return "compseasonpersonsport";
    }

    public List<CompSeasonPersonSportKey> getCompSeasonForPersonSports(List<Integer> personSportIds) throws SQLException {
        return getSuperKeyList("personsportid IN (" + getCommaSepIntList(personSportIds) + ")");
    }

    public void insertNonExistingPersonSports(Set<CompSeasonPersonSportKey> keys) throws SQLException {
        insertNonExistingKeys(keys);
    }

    public void insertCompSeasonPersonSport(CompSeasonPersonSportKey cspsKey) throws SQLException {
        insert(cspsKey);
    }
}
