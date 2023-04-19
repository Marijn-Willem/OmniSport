package com.sports.entity.key;

public class TeamMatchActionKey extends SuperKey {
    private TeamMatchKey teamMatchKey;
    private int teamMatchActionId;

    public TeamMatchActionKey(TeamMatchKey teamMatchKey, int teamMatchActionId) {
        this.teamMatchKey = teamMatchKey;
        this.teamMatchActionId = teamMatchActionId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TeamMatchActionKey &&
                ((TeamMatchActionKey)o).teamMatchKey.equals(teamMatchKey) &&
                ((TeamMatchActionKey)o).getTeamMatchActionId() == teamMatchActionId;
    }

    @Override
    public String getSepValues(String delim) {
        return teamMatchKey.getSepValues(delim) + delim + teamMatchActionId;
    }

    @Override
    public int hashCode() {
        return 100 * teamMatchKey.hashCode() + teamMatchActionId;
    }

    @Override
    public String getWhereClause() {
        return teamMatchKey.getWhereClause() + " AND teammatchactionid = " + teamMatchActionId;
    }

    @Override
    public TeamMatchKey getSuperKey() {
        return teamMatchKey;
    }

    private int getTeamMatchActionId() {
        return teamMatchActionId;
    }
}
