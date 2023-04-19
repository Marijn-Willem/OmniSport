package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Sport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportManager extends IntAliasableManager<Sport> {
    public SportManager(Statement stat) {
        super(stat);
    }

    @Override
    Sport getInstance() {
        return new Sport();
    }

    @Override
    String getTableName() {
        return "sport";
    }

    @Override
    String getKeyColumnString() {
        return "id";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "name",
                "isteam",
                "ish2h",
                "hasmatchparts"
            };
    }

    @Override
    Sport getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Sport sport = new Sport();

        sport.setId(rs.getInt("id"));
        sport.setName(rs.getString("name"));
        sport.setTeam(rs.getBoolean("isteam"));
        sport.setH2H(rs.getBoolean("ish2h"));
        sport.setHasMatchParts(rs.getBoolean("hasmatchparts"));

        return sport;
    }

    public int getIdFromName(String name) throws SQLException {
        return getIdFromColumnValue("sport", "name", name);
    }

    public List<Sport> getTeamSportsList() throws SQLException {
        return getSportList("isteam = " + QueryUtil.convertBooleanToDbValue(true));
    }

    public List<Sport> getH2HSportList() throws SQLException {
        return getSportList("ish2h = " + QueryUtil.convertBooleanToDbValue(true));
    }

    public List<Sport> getAlcifoSportList() throws SQLException {
        return getSportList("isteam = " + QueryUtil.convertBooleanToDbValue(false) + " AND " +
                "ish2h = " + QueryUtil.convertBooleanToDbValue(false));
    }

    public List<Sport> getFullSportList() throws SQLException {
        return getSportList(null);
    }

    public List<Sport> getTeamSportsList(List<Integer> sportIds) throws SQLException {
        return getEntityListFromIds(sportIds);
    }

    public Sport getSport(int sportId) throws SQLException {
        return getEntityFromId(sportId);
    }

    private List<Sport> getSportList(String whereClause) throws SQLException {
        return getEntityList(whereClause);
    }
}
