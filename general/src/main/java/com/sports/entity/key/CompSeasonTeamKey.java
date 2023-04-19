package com.sports.entity.key;

public class CompSeasonTeamKey extends CompSeasonParticipantKey {
    public CompSeasonTeamKey(CompSeasonKey compSeasonKey, int specificId) {
        super(compSeasonKey, specificId);
    }

    String getSpecificIdName() {
        return "teamid";
    }
}
