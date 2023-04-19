package com.sports.entity.manager;

import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.key.CompSeasonPhasePersonSportKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonPhasePersonSportManager extends CompSeasonPhaseParticipantManager<CompSeasonPersonSportKey,
        CompSeasonPhasePersonSportKey, SuperKeyEntity> {
    public CompSeasonPhasePersonSportManager(Statement stat) {
        super(stat);
    }

    String getSpecificIdColumn() {
        return "personsportid";
    }

    CompSeasonPhasePersonSportKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPhasePersonSportKey(
                ((CompSeasonPhaseManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getSpecificIdColumn()));
    }

    String getTableName() {
        return "compseasonphasepersonsport";
    }
}
