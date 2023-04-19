package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.SportDiscipline;
import com.sports.entity.key.SportDisciplineKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class SportDisciplineManager extends SuperKeyAliasableManager<SportDisciplineKey, SportDiscipline> {
    public SportDisciplineManager(Statement stat) {
        super(stat);
    }

    @Override
    SportDiscipline getInstance() {
        return new SportDiscipline();
    }

    @Override
    String getTableName() {
        return "sportdiscipline";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name", "resulttypeid", "resulttypeprecisionid" };
    }

    @Override
    String getKeyColumnString() {
        return "sportid, sportdisciplineid";
    }

    @Override
    SportDiscipline getInstanceFromResultSet(ResultSet rs) throws SQLException {
        SportDiscipline sportDiscipline = new SportDiscipline();

        sportDiscipline.setSportDisciplineId(rs.getInt("sportdisciplineid"));
        sportDiscipline.setName(rs.getString("name"));
        sportDiscipline.setResultTypeId(rs.getInt("resulttypeid"));
        sportDiscipline.setResultTypePrecisionId(QueryUtil.getIntegerFromResultSet(rs, "resulttypeprecisionid"));

        return sportDiscipline;
    }

    @Override
    SportDisciplineKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new SportDisciplineKey(rs.getInt("sportid"), rs.getInt("sportdisciplineid"));
    }

    public List<SportDiscipline> getSportDisciplineList(List<SportDisciplineKey> keys)
        throws SQLException {
        return getEntityListFromSuperKeys(keys);
    }

    public Map<SportDisciplineKey, SportDiscipline> getSportDisciplineMap(List<SportDisciplineKey> sportDisciplineKeys)
        throws SQLException {
        return getSuperKeyEntityMap(getConditionsKeyList(sportDisciplineKeys));
    }

    public List<SportDiscipline> getSportDisciplinesForSport(int sportId) throws SQLException {
        return getEntityList("sportid = " + sportId);
    }

    public int getNewDisciplineId(int sportId) throws SQLException {
        return getNewInt("sportdisciplineid", "sportid = " + sportId);
    }

    public void update(SportDisciplineKey sdk, SportDiscipline sd) throws SQLException {
        super.update(sdk, sd);
    }

    public void insert(SportDisciplineKey sdk, SportDiscipline sd) throws SQLException {
        super.insert(sdk, sd);
    }
}
