package com.sports.entity.key;

public class EventPartLocationKey extends SuperKey {
    private final CompSeasonEventPartKey compSeasonEventPartKey;
    private final int eventPartLocationId;

    public EventPartLocationKey(CompSeasonEventPartKey compSeasonEventPartKey, int eventPartLocationId) {
        this.compSeasonEventPartKey = compSeasonEventPartKey;
        this.eventPartLocationId = eventPartLocationId;
    }

    @Override
    public int hashCode() {
        return 100 * compSeasonEventPartKey.hashCode() + eventPartLocationId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventPartLocationKey &&
                ((EventPartLocationKey)obj).compSeasonEventPartKey == compSeasonEventPartKey &&
                ((EventPartLocationKey)obj).eventPartLocationId == eventPartLocationId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonEventPartKey.getWhereClause() + " AND eventpartlocationid = " + eventPartLocationId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonEventPartKey.getSepValues(delim) + delim + eventPartLocationId;
    }

    @Override
    public CompSeasonEventPartKey getSuperKey() {
        return compSeasonEventPartKey;
    }

    public int getEventPartLocationId() {
        return eventPartLocationId;
    }
}
