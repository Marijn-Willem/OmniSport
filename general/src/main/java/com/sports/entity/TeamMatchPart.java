package com.sports.entity;

public class TeamMatchPart extends H2HMatchPart {
    private int teamMatchPartId;

    public int getMatchPartId() {
        return teamMatchPartId;
    }

    String[] getSpecificPropertiesInSQLStrings() {
        return new String[0];
    }

    @Override
    void copySpecific(H2HMatchPart other) {

    }

    public void setParticipant1Win(boolean participant1Win) {

    }

    public int getTeamMatchPartId() {
        return teamMatchPartId;
    }

    public void setTeamMatchPartId(int teamMatchPartId) {
        this.teamMatchPartId = teamMatchPartId;
    }
}
