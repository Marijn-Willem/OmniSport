package com.sports.entity.manager;

import com.sports.entity.TeamMatchAction;
import com.sports.entity.key.TeamMatchActionKey;
import com.sports.entity.key.TeamMatchKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamMatchActionManager extends SuperKeySuperManager<TeamMatchActionKey, TeamMatchAction> {
    public TeamMatchActionManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "teammatchaction";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", teammatchactionid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "actiontypeid",
                "teamid",
                "count"
            };
    }

    @Override
    TeamMatchActionKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new TeamMatchActionKey(((TeamMatchManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("teammatchactionid"));
    }

    @Override
    protected TeamMatchAction getInstanceFromResultSet(ResultSet rs) throws SQLException {
        TeamMatchAction matchAction = new TeamMatchAction();

        matchAction.setTeamMatchId(rs.getInt("teammatchid"));
        matchAction.setActionTypeId(rs.getInt("actiontypeid"));
        matchAction.setTeamId(rs.getInt("teamid"));
        matchAction.setCount(rs.getInt("count"));

        return matchAction;
    }

    @Override
    TeamMatchManager getSuperManager() {
        return new TeamMatchManager(stat);
    }

    public void addMatchActions(Map<TeamMatchActionKey, TeamMatchAction> matchActionMap) throws SQLException {
        insert(matchActionMap);
    }

    public List<TeamMatchAction> getMatchActionsMatch(TeamMatchKey mk) throws SQLException {
        return getEntityList(mk.getWhereClause());
    }

    public List<TeamMatchAction> getMatchActionsMatches(List<TeamMatchKey> teamMatchKeys, List<Integer> actionTypeIds)
            throws SQLException {
        List<TeamMatchAction> matchActions = new ArrayList<>();

        if (!teamMatchKeys.isEmpty() && !actionTypeIds.isEmpty())
            matchActions.addAll(getEntityList("(" + getConditionsKeyList(teamMatchKeys) + ") " +
                    "AND actiontypeid IN (" + getCommaSepIntList(actionTypeIds) + ")"));

        return matchActions;
    }

    public int getNewTeamMatchActionId(TeamMatchKey teamMatchKey) throws SQLException {
        return getNewInt("teammatchactionid", teamMatchKey.getWhereClause());
    }
}
