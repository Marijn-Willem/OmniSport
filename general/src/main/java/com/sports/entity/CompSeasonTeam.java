package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class CompSeasonTeam extends SuperKeyEntity {
    private Integer compDivisionId;

    private int teamId;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(compDivisionId)
        };
    }

    public Integer getCompDivisionId() {
        return compDivisionId;
    }

    public void setCompDivisionId(Integer compDivisionId) {
        this.compDivisionId = compDivisionId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
