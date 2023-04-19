package com.sports.entity;

import com.sports.db.util.QueryUtil;

public abstract class AlcifoParticipant extends SuperKeyEntity {
    private Integer rank;
    private Integer noCountResultId;

    private int specificId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(rank),
                QueryUtil.convertIntegerToDbValue(noCountResultId)
        };
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

    public int getSpecificId() {
        return specificId;
    }

    public void setSpecificId(int specificId) {
        this.specificId = specificId;
    }
}
