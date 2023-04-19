package com.sports.entity;

public class CompSeasonPhaseTeam extends SuperKeyEntity {
    private final static int pointsCorrectionDefault = 0;

    private int pointsCorrection = pointsCorrectionDefault;

    private int teamId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                "" + pointsCorrection
        };
    }

    public int getPointsCorrection() {
        return pointsCorrection;
    }

    public void setPointsCorrection(int pointsCorrection) {
        this.pointsCorrection = pointsCorrection;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
