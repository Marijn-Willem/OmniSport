package com.sports.entity.key;

public class DisciplinePartTeamKey extends SuperKey {
    private final EventPartTeamKey eventPartTeamKey;
    private final int eventDisciplinePartId;

    public DisciplinePartTeamKey(EventPartTeamKey eventPartTeamKey, int eventDisciplinePartId) {
        this.eventPartTeamKey = eventPartTeamKey;
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    @Override
    public int hashCode() {
        return 100 * eventPartTeamKey.hashCode() + eventDisciplinePartId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DisciplinePartTeamKey &&
                ((DisciplinePartTeamKey)obj).eventPartTeamKey.equals(eventPartTeamKey) &&
                ((DisciplinePartTeamKey)obj).eventDisciplinePartId == eventDisciplinePartId;
    }

    @Override
    public String getWhereClause() {
        return eventPartTeamKey.getWhereClause() + " AND eventdisciplinepartid = " + eventDisciplinePartId;
    }

    @Override
    public String getSepValues(String delim) {
        return eventPartTeamKey.getSepValues(delim) + delim + eventDisciplinePartId;
    }

    @Override
    public EventPartTeamKey getSuperKey() {
        return eventPartTeamKey;
    }
}
