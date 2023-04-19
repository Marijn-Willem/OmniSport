package com.sports.entity.key;

public class EventTeamKey extends AlcifoParticipantKey {
    @Override
    String getParticipantIdColumn() {
        return "teamid";
    }

    public EventTeamKey(CompSeasonEventKey compSeasonEventKey, int teamId) {
        super(compSeasonEventKey, teamId);
    }
}
