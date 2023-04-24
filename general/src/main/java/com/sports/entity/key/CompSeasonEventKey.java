package com.sports.entity.key;

public class CompSeasonEventKey extends SuperKey {
    private final CompSeasonKey compSeasonKey;
    private final int compSeasonEventId;

    public CompSeasonEventKey(CompSeasonKey csk, int compSeasonEventId) {
        this.compSeasonKey = csk;
        this.compSeasonEventId = compSeasonEventId;
    }

    @Override
    public int hashCode() {
        return 100 * getSuperKey().hashCode() + compSeasonEventId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonEventKey &&
                ((CompSeasonEventKey)obj).getSuperKey().equals(compSeasonKey) &&
                ((CompSeasonEventKey)obj).compSeasonEventId == compSeasonEventId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonKey.getSepValues(delim) + delim + compSeasonEventId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonKey.getWhereClause() + " AND compSeasonEventId = " + compSeasonEventId;
    }

    @Override
    public CompSeasonKey getSuperKey() {
        return compSeasonKey;
    }

    public int getCompSeasonEventId() {
        return compSeasonEventId;
    }
}
