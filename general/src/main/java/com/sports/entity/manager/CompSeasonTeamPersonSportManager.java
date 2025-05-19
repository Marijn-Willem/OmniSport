package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.key.CompSeasonTeamPersonSportKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonTeamPersonSportManager extends SuperKeySuperManager<CompSeasonTeamPersonSportKey, SuperKeyEntity> {
    public CompSeasonTeamPersonSportManager(Statement stat) {
        super(stat);
    }

    CompSeasonTeamPersonSportKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonTeamPersonSportKey(
                ((CompSeasonTeamManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("personsportid"));
    }

    String getTableName() {
        return "compseasonteampersonsport";
    }

    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", personsportid";
    }

    String[] getValueColumns() {
        return new String[0];
    }

    SuperKeyEntity getInstanceFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    CompSeasonTeamManager getSuperManager() {
        return new CompSeasonTeamManager(stat);
    }

    public List<CompSeasonTeamPersonSportKey> getTeamPersonSports(List<CompSeasonPersonSportKey> keys)
        throws SQLException {
        return getSuperKeyEntityMapFromSuperKeys(keys).keySet().stream().toList();
    }

    public List<CompSeasonTeamPersonSportKey> getTeamPersonSportsForTeam(CompSeasonTeamKey key) throws SQLException {
        return getSuperKeyList(key.getWhereClause());
    }

    public void insertTeamPersonSports(List<CompSeasonTeamPersonSportKey> compSeasonTeamPersonSportKeys)
        throws SQLException {
        insert(compSeasonTeamPersonSportKeys);
    }

    public void deleteTeamPersonSports(List<CompSeasonPersonSportKey> compSeasonPersonSportKeys) throws SQLException {
        delete(compSeasonPersonSportKeys);
    }

    public List<CompSeasonTeamPersonSportKey> getKeysForCompSeasonPerson(CompSeasonPersonSportKey key)
        throws SQLException {
        return getSuperKeyList(key.getWhereClause());
    }

    public List<CompSeasonTeamPersonSportKey> getKeysForCompSeason(CompSeasonKey key) throws SQLException {
        return getSuperKeyList(key.getWhereClause());
    }
}
