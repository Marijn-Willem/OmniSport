package com.sports.entity.manager;

import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.key.CompSeasonTeamKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonPhaseTeamManager extends CompSeasonPhaseParticipantManager<CompSeasonTeamKey, CompSeasonPhaseTeamKey> {
    public CompSeasonPhaseTeamManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasonphaseteam";
    }

    @Override
    String getSpecificIdColumn() {
        return "teamid";
    }

    @Override
    CompSeasonPhaseTeamKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPhaseTeamKey(((CompSeasonPhaseManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getSpecificIdColumn()));
    }

    public List<Integer> getTeamsInCompSeasonPhases(List<CompSeasonPhaseKey> keys)
            throws SQLException {
        return getParticipantsInCompSeasonPhases(keys);
    }
}
