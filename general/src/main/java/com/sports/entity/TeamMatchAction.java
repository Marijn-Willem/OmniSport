package com.sports.entity;

public class TeamMatchAction extends SuperKeyEntity {
    private int actionTypeId;
    private int teamId;
    private int count;

    private int teamMatchId;
    private int matchSort;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                "" + actionTypeId,
                "" + teamId,
                "" + count
            };
    }

    public int getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTeamMatchId() {
        return teamMatchId;
    }

    public void setTeamMatchId(int teamMatchId) {
        this.teamMatchId = teamMatchId;
    }

    public int getMatchSort() {
        return matchSort;
    }

    public void setMatchSort(int matchSort) {
        this.matchSort = matchSort;
    }
}
