package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.TeamMatch;
import com.sports.entity.TeamMatchAction;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.TeamMatchActionKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.logic.calculation.Calculation;

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
                "minute",
                "teammatchpartid",
                "teamid"
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
        matchAction.setMinute(QueryUtil.getIntegerFromResultSet(rs, "minute"));
        matchAction.setActionTypeId(rs.getInt("actiontypeid"));
        matchAction.setTeamMatchPartId(QueryUtil.getIntegerFromResultSet(rs, "teammatchpartid"));
        matchAction.setTeamId(rs.getInt("teamid"));

        return matchAction;
    }

    @Override
    TeamMatchManager getSuperManager() {
        return new TeamMatchManager(stat);
    }

    public void addMatchActions(Map<TeamMatchActionKey, TeamMatchAction> matchActionMap) throws SQLException {
        insert(matchActionMap);
    }

    public List<TeamMatchAction> getMatchActionsCompSeasonClub(List<TeamMatch> teamMatches, CompSeasonKey csk,
                                                               int teamId, int actionTypeId) throws SQLException {
        List<TeamMatchAction> matchActions = new ArrayList<TeamMatchAction>();

        if (teamMatches.size() > 0)
            matchActions = getEntityList("(" +
                    Calculation.getWhereClauseTeamInMatches(csk, teamId, teamMatches) +
                    ") AND actiontypeid = " + actionTypeId);

        return matchActions;
    }

    public List<TeamMatchAction> getMatchActionsMatch(TeamMatchKey mk) throws SQLException {
        return getEntityList(mk.getWhereClause());
    }

    public List<TeamMatchAction> getMatchActionsMatches(List<TeamMatchKey> teamMatchKeys, List<Integer> actionTypeIds)
            throws SQLException {
        List<TeamMatchAction> matchActions = new ArrayList<TeamMatchAction>();

        if (teamMatchKeys.size() > 0 && actionTypeIds.size() > 0)
            matchActions = getEntityList("(" + getConditionsKeyList(teamMatchKeys) + ") " +
                    "AND actiontypeid IN (" + getCommaSepIntList(actionTypeIds) + ")");

        return matchActions;
    }

    public void deleteMatchActions(List<TeamMatchKey> teamMatchKeys) throws SQLException {
        delete(teamMatchKeys);
    }

    public void updateTeamMatchActions(Map<TeamMatchActionKey, TeamMatchAction> matchActionMap) throws SQLException {
        updateEntityMap(matchActionMap);
    }

    public int getNewTeamMatchActionId(TeamMatchKey teamMatchKey) throws SQLException {
        return getNewInt("teammatchactionid", teamMatchKey.getWhereClause());
    }
}
