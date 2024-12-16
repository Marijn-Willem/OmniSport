package com.sports.entity.manager;

import com.sports.entity.key.CompSeasonDoubleKey;
import com.sports.entity.key.CompSeasonPhaseDoubleKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPhaseDoubleManager extends CompSeasonPhaseParticipantManager<CompSeasonDoubleKey, CompSeasonPhaseDoubleKey> {
    public CompSeasonPhaseDoubleManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasonphasedouble";
    }

    @Override
    String getSpecificIdColumn() {
        return "doubleid";
    }

    @Override
    CompSeasonPhaseDoubleKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPhaseDoubleKey(((CompSeasonPhaseManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getSpecificIdColumn()));
    }
}
