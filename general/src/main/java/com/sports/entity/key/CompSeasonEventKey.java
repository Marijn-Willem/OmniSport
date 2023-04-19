package com.sports.entity.key;

public class CompSeasonEventKey extends SuperKey {
    private CompSeasonKey compSeasonKey;
    private int sportId;
    private int sportEventId;

    public CompSeasonEventKey(CompSeasonKey csk, int sportId, int sportEventId) {
        this.compSeasonKey = csk;
        this.sportId = sportId;
        this.sportEventId = sportEventId;
    }

    @Override
    public int hashCode() {
        return 10000 * getSuperKey().hashCode() + 100 * sportId + sportEventId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompSeasonEventKey &&
                ((CompSeasonEventKey)obj).getSuperKey().equals(compSeasonKey) &&
                ((CompSeasonEventKey)obj).getSportId() == sportId &&
                ((CompSeasonEventKey)obj).getSportEventId() == sportEventId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonKey.getSepValues(delim) + delim + sportId + delim + sportEventId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonKey.getWhereClause() + " AND sportId = " + sportId + " AND sportEventId = " + sportEventId;
    }

    @Override
    public CompSeasonKey getSuperKey() {
        return compSeasonKey;
    }

    public int getSportId() {
        return sportId;
    }

    public int getSportEventId() {
        return sportEventId;
    }

    public SportEventKey getSportEventKey() {
        return new SportEventKey(sportId, sportEventId);
    }
}
