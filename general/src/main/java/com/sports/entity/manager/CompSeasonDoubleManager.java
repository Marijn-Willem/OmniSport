package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonDoubleKey;
import com.sports.entity.key.CompSeasonKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonDoubleManager extends CompSeasonParticipantManager<CompSeasonDoubleKey, SuperKeyEntity> {
    public CompSeasonDoubleManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasondouble";
    }

    public String getIdColumn() {
        return "doubleid";
    }

    @Override
    CompSeasonDoubleKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonDoubleKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    public List<Integer> getCompSeasonDoubleIds(CompSeasonKey compSeasonKey) throws SQLException {
        return getParticipantIdsCompSeason(compSeasonKey);
    }

    public void insertCompSeasonDoubleKeys(List<CompSeasonDoubleKey> keys) throws SQLException {
        insert(keys);
    }
}
