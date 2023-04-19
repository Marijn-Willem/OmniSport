package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.CompDivision;
import com.sports.entity.key.CompDivisionKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class CompDivisionManager extends SuperKeySuperManager<CompDivisionKey, CompDivision> {
    public CompDivisionManager(Statement stat) {
        super(stat);
    }

    String getTableName() {
        return "compdivision";
    }

    String getKeyColumnString() {
        return "competitionid, compdivisionid";
    }

    String[] getValueColumns() {
        return new String[] {
                "name",
                "parentdivisionid"
        };
    }

    CompDivision getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompDivision compDivision = new CompDivision();

        compDivision.setName(rs.getString("name"));
        compDivision.setParentDivisionId(QueryUtil.getIntegerFromResultSet(rs, "parentdivisionid"));
        compDivision.setCompDivisionId(rs.getInt("compdivisionid"));

        return compDivision;
    }

    @Override
    CompDivisionKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompDivisionKey(rs.getInt("competitionid"), rs.getInt("seasonid"));
    }

    public CompDivision getCompDivision(CompDivisionKey compDivisionKey) throws SQLException {
        return getEntityFromSuperKey(compDivisionKey);
    }

    public List<CompDivision> getCompDivisions(int competitionId) throws SQLException {
        return getEntityList("competitionid = " + competitionId);
    }

    public List<CompDivision> getChildDivisions(CompDivisionKey compDivisionKey) throws SQLException {
        return getChildEntities(Collections.singletonList(compDivisionKey));
    }

    public List<CompDivision> getCompDivisionList(List<CompDivisionKey> keyList) throws SQLException {
        return getEntityListFromSuperKeys(keyList);
    }

    public int getNewDivisionId(int competitionId) throws SQLException {
        return getNewInt("compdivisionid", "competitionid = " + competitionId);
    }
}
