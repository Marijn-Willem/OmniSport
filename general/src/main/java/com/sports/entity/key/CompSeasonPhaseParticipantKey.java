package com.sports.entity.key;

public abstract class CompSeasonPhaseParticipantKey extends SuperKey {
    private final CompSeasonPhaseKey compSeasonPhaseKey;
    private final int specificId;

    public CompSeasonPhaseParticipantKey(CompSeasonPhaseKey compSeasonPhaseKey, int specificId) {
        this.compSeasonPhaseKey = compSeasonPhaseKey;
        this.specificId = specificId;
    }

    abstract String getSpecificIdName();

    @Override
    public int hashCode() {
        return 1000 * compSeasonPhaseKey.hashCode() + specificId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonPhaseParticipantKey &&
                ((CompSeasonPhaseParticipantKey)obj).getSuperKey().equals(compSeasonPhaseKey) &&
                ((CompSeasonPhaseParticipantKey)obj).specificId == specificId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonPhaseKey.getWhereClause() + " AND " + getSpecificIdName() + " = " + specificId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonPhaseKey.getSepValues(delim) + delim + specificId;
    }

    @Override
    public CompSeasonPhaseKey getSuperKey() {
        return compSeasonPhaseKey;
    }

    public int getSpecificId() {
        return specificId;
    }
}
