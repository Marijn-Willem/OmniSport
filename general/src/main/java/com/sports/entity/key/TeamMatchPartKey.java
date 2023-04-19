package com.sports.entity.key;

public class TeamMatchPartKey extends H2HMatchPartKey {
    public TeamMatchPartKey(TeamMatchKey teamMatchKey, int teamMatchPartId) {
        super(teamMatchKey, teamMatchPartId);
    }

    String getSpecificIdName() {
        return "teammatchpartid";
    }
}
