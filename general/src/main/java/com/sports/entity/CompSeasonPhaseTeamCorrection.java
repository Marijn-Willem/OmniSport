package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.time.LocalDateTime;

public class CompSeasonPhaseTeamCorrection extends SuperKeyEntity {
    private LocalDateTime date;
    private int pointsCorrection;

    private int teamId;
    private int compSeasonPhaseTeamCorrectionId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertDateTimeToDbString(date),
                "" + pointsCorrection
        };
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public int getCompSeasonPhaseTeamCorrectionId() {
        return compSeasonPhaseTeamCorrectionId;
    }

    public void setCompSeasonPhaseTeamCorrectionId(int compSeasonPhaseTeamCorrectionId) {
        this.compSeasonPhaseTeamCorrectionId = compSeasonPhaseTeamCorrectionId;
    }
}
