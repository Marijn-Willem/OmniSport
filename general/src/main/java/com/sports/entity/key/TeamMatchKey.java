package com.sports.entity.key;

public class TeamMatchKey extends H2HMatchKey {
    public TeamMatchKey(CompSeasonKey compSeasonKey, int teamMatchId) {
        super(compSeasonKey, teamMatchId);
    }

    String getSpecificIdName() {
        return "teammatchid";
    }

    public int getTeamMatchId() {
        return getSpecificId();
    }
}
