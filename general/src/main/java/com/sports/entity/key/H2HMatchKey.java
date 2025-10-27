package com.sports.entity.key;

public abstract class H2HMatchKey extends SuperKey implements EntityKeyWithParent {
    private final CompSeasonKey compSeasonKey;
    private final int specificId;

    public H2HMatchKey(CompSeasonKey compSeasonKey, int specificId) {
        this.compSeasonKey = compSeasonKey;
        this.specificId = specificId;
    }

    @Override
    public int hashCode() {
        return 1000 * compSeasonKey.hashCode() + specificId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof H2HMatchKey &&
                ((H2HMatchKey)obj).compSeasonKey.equals(compSeasonKey) &&
                ((H2HMatchKey)obj).specificId == specificId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonKey.getSepValues(delim) + delim + specificId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonKey.getWhereClause() + " AND " + getSpecificIdName() + " = " + specificId;
    }

    @Override
    public CompSeasonKey getSuperKey() {
        return compSeasonKey;
    }

    @Override
    public String getWhereClauseParent() {
        return compSeasonKey.getWhereClause() + " AND parentmatchid = " + specificId;
    }

    abstract String getSpecificIdName();

    public int getSpecificId() {
        return specificId;
    }

    public int getCompetitionId() {
        return compSeasonKey.getCompetitionId();
    }

    public int getSeasonId() {
        return compSeasonKey.getSeasonId();
    }
}
