package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.logic.calculation.Calculation;

public class Team extends Participant {
    private Integer clubId;
    private Integer nocId;
    private Integer equipeId;
    private int sportId;
    private int genderId;

    private int bonusPoints;
    private boolean hasCup;
    private CompDivision compDivision;
    private int playedParentDivision;
    private int winsParentDivision;
    private int drawsParentDivision;
    private int playedDivision;
    private int winsDivision;
    private int drawsDivision;
    private int sortIndex;

    public void copyToForStanding(Team other) {
        other.setClubId(clubId);
        other.setNocId(nocId);
        other.setEquipeId(equipeId);
        other.setSportId(sportId);
        other.setGenderId(genderId);

        other.setBonusPoints(bonusPoints);
        other.setCompDivision(compDivision);
        other.setPlayedParentDivision(playedParentDivision);
        other.setWinsParentDivision(winsParentDivision);
        other.setDrawsParentDivision(drawsParentDivision);
        other.setPlayedDivision(playedDivision);
        other.setWinsDivision(winsDivision);
        other.setDrawsDivision(drawsDivision);

        copyStandingFieldsTo(other);
    }

    public double getParentDivisionAverage() {
        return Calculation.getAverage(playedParentDivision, winsParentDivision, drawsParentDivision);
    }

    public double getDivisionAverage() {
        return Calculation.getAverage(playedDivision, winsDivision, drawsDivision);
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public Integer getNocId() {
        return nocId;
    }

    public void setNocId(Integer nocId) {
        this.nocId = nocId;
    }

    public Integer getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(Integer equipeId) {
        this.equipeId = equipeId;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public void addBonusPoint() {
        bonusPoints++;
    }

    public boolean isHasCup() {
        return hasCup;
    }

    public void setHasCup(boolean hasCup) {
        this.hasCup = hasCup;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public CompDivision getCompDivision() {
        return compDivision;
    }

    public void setCompDivision(CompDivision compDivision) {
        this.compDivision = compDivision;
    }

    public int getPlayedParentDivision() { return playedParentDivision; }

    public void setPlayedParentDivision(int playedParentDivision) {
        this.playedParentDivision = playedParentDivision;
    }

    public void addPlayedParentDivision() { playedParentDivision++; }

    public int getWinsParentDivision() { return winsParentDivision; }

    public void setWinsParentDivision(int winsParentDivision) {
        this.winsParentDivision = winsParentDivision;
    }

    public void addWinParentDivision() { winsParentDivision++; }

    public int getDrawsParentDivision() { return drawsParentDivision; }

    public void setDrawsParentDivision(int drawsParentDivision) {
        this.drawsParentDivision = drawsParentDivision;
    }

    public void addDrawParentDivision() { drawsParentDivision++; }

    public int getPlayedDivision() { return playedDivision; }

    public void setPlayedDivision(int playedDivision) {
        this.playedDivision = playedDivision;
    }

    public void addPlayedDivision() { playedDivision++; }

    public int getWinsDivision() { return winsDivision; }

    public void setWinsDivision(int winsDivision) {
        this.winsDivision = winsDivision;
    }

    public void addWinDivision() { winsDivision++; }

    public int getDrawsDivision() { return drawsDivision; }

    public void setDrawsDivision(int drawsDivision) {
        this.drawsDivision = drawsDivision;
    }

    public void addDrawDivision() { drawsDivision++; }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(clubId),
                QueryUtil.convertIntegerToDbValue(nocId),
                QueryUtil.convertIntegerToDbValue(equipeId),
                "" + sportId,
                "" + genderId
            };
    }
}
