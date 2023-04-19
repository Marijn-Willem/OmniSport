package com.sports.entity;

public class EventPartTeam extends AlcifoPartParticipant {
    private int teamId;

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[0];
    }

    @Override
    public int getParticipantId() {
        return getTeamId();
    }

    @Override
    public void setParticipantId(int participantId) {
        setTeamId(participantId);
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
