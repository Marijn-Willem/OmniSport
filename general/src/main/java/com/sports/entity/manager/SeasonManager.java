package com.sports.entity.manager;

import com.sports.entity.Season;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SeasonManager extends IntSuperManager<Season> {
    public SeasonManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "season";
    }

    @Override
    String getKeyColumnString() {
        return "id";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name", "\"order\"" };
    }

    @Override
    Season getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Season season = new Season();
        season.setId(rs.getInt("id"));
        season.setName(rs.getString("name"));
        season.setOrder(rs.getInt("order"));

        return season;
    }

    public Season getSeason(int id) throws SQLException {
        return getEntityFromId(id);
    }

    public List<Season> getSeasonList(List<Integer> ids) throws SQLException {
        return getEntityListFromIds(ids);
    }

    public List<Season> getAllSeasons() throws SQLException {
        return getEntityList(null);
    }

    public int getNewId() throws SQLException {
        return super.getNewId();
    }

    public void insert(int id, Season season) throws SQLException {
        super.insert(id, season);
    }

    public void update(int id, Season season) throws SQLException {
        super.update(id, season);
    }
}
