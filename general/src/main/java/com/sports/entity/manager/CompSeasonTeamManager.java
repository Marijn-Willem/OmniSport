package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.CompSeasonTeam;
import com.sports.entity.key.CompSeasonDivisionKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonTeamKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CompSeasonTeamManager extends CompSeasonParticipantManager<CompSeasonTeamKey, CompSeasonTeam> {
    public CompSeasonTeamManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasonteam";
    }

    public String getIdColumn() {
        return "teamid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "compdivisionid" };
    }

    public List<CompSeasonTeamKey> getTeamsInCompSeasonDivision(List<CompSeasonDivisionKey> keys) throws SQLException {
        return getSuperKeyList(getConditionsKeyList(keys));
    }

    public List<CompSeasonTeam> getTeamsInCompSeason(CompSeasonKey csk) throws SQLException {
        return getEntityList(csk.getWhereClause());
    }

    public CompSeasonTeam getCompSeasonTeam(CompSeasonTeamKey compSeasonTeamKey) throws SQLException {
        return getEntityFromSuperKey(compSeasonTeamKey);
    }

    public void insertCompSeasonTeamMap(Map<CompSeasonTeamKey, CompSeasonTeam> compSeasonTeamMap) throws SQLException {
        insert(compSeasonTeamMap);
    }

    @Override
    CompSeasonTeamKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonTeamKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    @Override
    CompSeasonTeam getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonTeam compSeasonTeam = new CompSeasonTeam();

        compSeasonTeam.setTeamId(rs.getInt("teamid"));
        compSeasonTeam.setCompDivisionId(QueryUtil.getIntegerFromResultSet(rs, "compdivisionid"));

        return compSeasonTeam;
    }
}
