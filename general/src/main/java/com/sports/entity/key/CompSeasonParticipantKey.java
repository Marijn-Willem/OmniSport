package com.sports.entity.key;

public abstract class CompSeasonParticipantKey extends SuperKey {
    private CompSeasonKey compSeasonKey;
    private int specificId;

    public CompSeasonParticipantKey(CompSeasonKey compSeasonKey, int specificId) {
        this.compSeasonKey = compSeasonKey;
        this.specificId = specificId;
    }

    abstract String getSpecificIdName();

    @Override
    public int hashCode() {
        return 1000 * getSuperKey().hashCode() + specificId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonParticipantKey &&
                ((CompSeasonParticipantKey)obj).getSuperKey().equals(compSeasonKey) &&
                ((CompSeasonParticipantKey)obj).specificId == specificId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonKey.getWhereClause() + " AND " + getSpecificIdName() + " = " + specificId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonKey.getSepValues(delim) + delim + specificId;
    }

    @Override
    public CompSeasonKey getSuperKey() {
        return compSeasonKey;
    }

    public int getSpecificId() {
        return specificId;
    }
}
