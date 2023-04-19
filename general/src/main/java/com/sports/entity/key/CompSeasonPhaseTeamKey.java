package com.sports.entity.key;

public class CompSeasonPhaseTeamKey extends CompSeasonPhaseParticipantKey {
    public CompSeasonPhaseTeamKey(CompSeasonPhaseKey cspk, int teamId) {
        super(cspk, teamId);
    }

    @Override
    String getSpecificIdName() {
        return "teamid";
    }

    public int getTeamId() {
        return getSpecificId();
    }
}
