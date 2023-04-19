package com.sports.entity.manager;

import com.sports.entity.NoCountResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class NoCountResultManager extends IntSuperManager<NoCountResult> {
    public NoCountResultManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "nocountresult";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    @Override
    NoCountResult getInstanceFromResultSet(ResultSet rs) throws SQLException {
        NoCountResult noCountResult = new NoCountResult();

        noCountResult.setId(rs.getInt("id"));
        noCountResult.setName(rs.getString("name"));

        return noCountResult;
    }

    public List<NoCountResult> getNoCountResults() throws SQLException {
        return getEntityList(null);
    }

    public Map<Integer, String> getIdNameMap() throws SQLException {
        return super.getIdNameMap();
    }

    public Map<String, Integer> getNameIdMap() throws SQLException {
        return super.getNameIdMap();
    }
}
