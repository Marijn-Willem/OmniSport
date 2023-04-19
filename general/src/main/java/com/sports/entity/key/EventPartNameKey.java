package com.sports.entity.key;

public class EventPartNameKey extends SuperKey {
    private final SportEventKey sportEventKey;
    private final int eventPartNameId;

    public EventPartNameKey(SportEventKey sportEventKey, int eventPartNameId) {
        this.sportEventKey = sportEventKey;
        this.eventPartNameId = eventPartNameId;
    }

    @Override
    public int hashCode() {
        return 100 * sportEventKey.hashCode() + eventPartNameId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventPartNameKey &&
                ((EventPartNameKey)obj).sportEventKey.equals(sportEventKey) &&
                ((EventPartNameKey)obj).eventPartNameId == eventPartNameId;
    }

    @Override
    public SuperKey getSuperKey() {
        return sportEventKey;
    }

    @Override
    public String getWhereClause() {
        return sportEventKey.getWhereClause() + " AND eventpartnameid = " + eventPartNameId;
    }

    @Override
    public String getSepValues(String delim) {
        return sportEventKey.getSepValues(delim) + delim + eventPartNameId;
    }

    public int getEventPartNameId() {
        return eventPartNameId;
    }
}
