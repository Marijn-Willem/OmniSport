package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class TeamMatchAction extends SuperKeyEntity {
    private int actionTypeId;
    private Integer minute;
    private Integer teamMatchPartId;
    private int teamId;

    private int teamMatchId;
    private int matchSort;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                "" + actionTypeId,
                QueryUtil.convertIntegerToDbValue(minute),
                QueryUtil.convertIntegerToDbValue(teamMatchPartId),
                "" + teamId
            };
    }

    public int getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getTeamMatchPartId() {
        return teamMatchPartId;
    }

    public void setTeamMatchPartId(Integer teamMatchPartId) {
        this.teamMatchPartId = teamMatchPartId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
