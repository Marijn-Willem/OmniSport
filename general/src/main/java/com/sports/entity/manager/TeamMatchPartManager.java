package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.TeamMatchPart;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.key.TeamMatchPartKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class TeamMatchPartManager extends H2HMatchPartManager<TeamMatchPartKey, TeamMatchPart> {
    public TeamMatchPartManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "teammatchpart";
    }

    public String getIdColumn() {
        return "teammatchpartid";
    }

    public String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    TeamMatchPartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new TeamMatchPartKey(((TeamMatchManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    @Override
    protected TeamMatchPart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        TeamMatchPart teamMatchPart = new TeamMatchPart();

        teamMatchPart.setTeamMatchPartId(rs.getInt("teammatchpartid"));
        teamMatchPart.setName(rs.getString("name"));
        teamMatchPart.setParentMatchPartId(QueryUtil.getIntegerFromResultSet(rs, "parentmatchpartid"));
        teamMatchPart.setFinished(rs.getBoolean("finished"));

        return teamMatchPart;
    }

    protected TeamMatchManager getSuperManager() {
        return new TeamMatchManager(stat);
    }

    public List<TeamMatchPart> getTeamMatchParts(TeamMatchKey teamMatchKey) throws SQLException {
        return getEntityList(teamMatchKey.getWhereClause());
    }

    public void insertMap(Map<TeamMatchPartKey, TeamMatchPart> matchPartMap) throws SQLException {
        insert(matchPartMap);
    }

    public void deleteMatchPartsFromMatches(List<TeamMatchKey> teamMatchKeys) throws SQLException {
        delete(teamMatchKeys);
    }
}
