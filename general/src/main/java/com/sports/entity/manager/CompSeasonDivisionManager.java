package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonDivisionKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonDivisionManager extends SuperKeySuperManager<CompSeasonDivisionKey, SuperKeyEntity> {
    public CompSeasonDivisionManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasondivision";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", compdivisionid";
    }

    @Override
    String[] getValueColumns() {
        return new String[0];
    }

    @Override
    SuperKeyEntity getInstanceFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    CompSeasonDivisionKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonDivisionKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("compdivisionid"));
    }

    @Override
    CompSeasonManager getSuperManager() {
        return new CompSeasonManager(stat);
    }

    public List<CompSeasonDivisionKey> getCompSeasonDivisions(CompSeasonKey compSeasonKey) throws SQLException {
        return getSuperKeyList(compSeasonKey.getWhereClause());
    }

    public void insertCompSeasonDivisions(List<CompSeasonDivisionKey> keys) throws SQLException {
        insert(keys);
    }
}
