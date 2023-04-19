package com.sports.entity.manager;

import com.sports.entity.StatType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatTypeManager extends IntAliasableManager<StatType> {
    public StatTypeManager(Statement stat) {
        super(stat);
    }

    @Override
    StatType getInstance() {
        return new StatType();
    }

    @Override
    String getTableName() {
        return "stattype";
    }

    @Override
    String getKeyColumnString() {
        return "id";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name" };
    }

    @Override
    StatType getInstanceFromResultSet(ResultSet rs) throws SQLException {
        StatType statType = new StatType();

        statType.setId(rs.getInt("id"));
        statType.setName(rs.getString("name"));

        return statType;
    }

    public List<StatType> getStatTypeList() throws SQLException {
        List<StatType> statTypeList = new ArrayList<StatType>();

        ResultSet rs = stat.executeQuery(getGenericQuery(null));

        while (rs.next())
            statTypeList.add(getInstanceFromResultSet(rs));

        return statTypeList;
    }
}
