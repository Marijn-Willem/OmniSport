package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.logic.util.Util;

public abstract class AlcifoPartParticipant extends SuperKeyEntity {
    private Integer points;
    private Integer rank;
    private Integer noCountResultId;

    private Integer calculatedRank;

    abstract String[] getSpecificPropertiesInSQLStrings();

    public abstract int getParticipantId();
    public abstract void setParticipantId(int participantId);

    @Override
    public String[] getPropertiesInSQLStrings() {
        String[] generalProps = new String[] {
                QueryUtil.convertIntegerToDbValue(points),
                QueryUtil.convertIntegerToDbValue(rank),
                QueryUtil.convertIntegerToDbValue(noCountResultId)
        };

        return Util.concatenateStringArrays(generalProps, getSpecificPropertiesInSQLStrings());
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getNoCountResultId() {
        return noCountResultId;
    }

    public void setNoCountResultId(Integer noCountResultId) {
        this.noCountResultId = noCountResultId;
    }

    public Integer getCalculatedRank() {
        return calculatedRank;
    }

    public void setCalculatedRank(Integer calculatedRank) {
        this.calculatedRank = calculatedRank;
    }
}
