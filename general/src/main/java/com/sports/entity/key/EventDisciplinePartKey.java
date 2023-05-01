package com.sports.entity.key;

public class EventDisciplinePartKey extends SuperKey {
    private final CompSeasonEventPartKey compSeasonEventPartKey;
    private final int eventDisciplinePartId;

    public EventDisciplinePartKey(CompSeasonEventPartKey compSeasonEventPartKey, int eventDisciplinePartId) {
        this.compSeasonEventPartKey = compSeasonEventPartKey;
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    @Override
    public int hashCode() {
        return 100 * compSeasonEventPartKey.hashCode() + eventDisciplinePartId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventDisciplinePartKey &&
                ((EventDisciplinePartKey)obj).compSeasonEventPartKey.equals(compSeasonEventPartKey) &&
                ((EventDisciplinePartKey) obj).eventDisciplinePartId == eventDisciplinePartId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonEventPartKey.getSepValues(delim) + delim + eventDisciplinePartId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonEventPartKey.getWhereClause() + " AND eventdisciplinepartid = " + eventDisciplinePartId;
    }

    @Override
    public CompSeasonEventPartKey getSuperKey() {
        return compSeasonEventPartKey;
    }

    public int getEventDisciplinePartId() {
        return eventDisciplinePartId;
    }
}
