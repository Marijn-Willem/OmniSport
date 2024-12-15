package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.CompSeasonPhaseTeamCorrection;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamCorrectionKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class CompSeasonPhaseTeamCorrectionManager extends SuperKeySuperManager<CompSeasonPhaseTeamCorrectionKey, CompSeasonPhaseTeamCorrection> {
    public CompSeasonPhaseTeamCorrectionManager(Statement stat) {
        super(stat);
    }

    @Override
    CompSeasonPhaseTeamCorrectionKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPhaseTeamCorrectionKey(((CompSeasonPhaseTeamManager)getCachedSuperManager())
                .getSuperKeyFromResultSet(rs), rs.getInt("compseasonphaseteamcorrectionid"));
    }

    @Override
    String getTableName() {
        return "compseasonphaseteamcorrection";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", compseasonphaseteamcorrectionid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "date",
                "pointscorrection"
        };
    }

    @Override
    CompSeasonPhaseTeamCorrection getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonPhaseTeamCorrection compSeasonPhaseTeamCorrection = new CompSeasonPhaseTeamCorrection();

        compSeasonPhaseTeamCorrection.setDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("date")));
        compSeasonPhaseTeamCorrection.setPointsCorrection(rs.getInt("pointscorrection"));
        compSeasonPhaseTeamCorrection.setTeamId(rs.getInt("teamid"));
        compSeasonPhaseTeamCorrection.setCompSeasonPhaseTeamCorrectionId(rs.getInt("compseasonphaseteamcorrectionid"));

        return compSeasonPhaseTeamCorrection;
    }

    @Override
    CompSeasonPhaseTeamManager getSuperManager() {
        return new CompSeasonPhaseTeamManager(stat);
    }

    public int getNewCompSeasonPhaseTeamCorrectionId(CompSeasonPhaseTeamKey compSeasonPhaseTeamKey) throws SQLException {
        return getNewInt("compseasonphaseteamcorrectionid", compSeasonPhaseTeamKey.getWhereClause());
    }

    public List<CompSeasonPhaseTeamCorrection> getCorrectionsForCompSeasonPhase(CompSeasonPhaseKey cspKey) throws SQLException {
        return getEntityListFromSuperKeys(Collections.singletonList(cspKey));
    }

    public List<CompSeasonPhaseTeamCorrection> getCorrectionsForCompSeasonPhaseTeam(CompSeasonPhaseTeamKey csptKey) throws SQLException {
        return getEntityListFromSuperKeys(Collections.singletonList(csptKey));
    }
}
