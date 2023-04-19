package com.sports.entity.key;

public class DisciplinePartPersonSportKey extends SuperKey {
    private EventPartPersonSportKey eventPartPersonSportKey;
    private int eventDisciplinePartId;

    public DisciplinePartPersonSportKey(EventPartPersonSportKey eppk, int eventDisciplinePartId) {
        this.eventPartPersonSportKey = eppk;
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    @Override
    public int hashCode() {
        return 100 * eventPartPersonSportKey.hashCode() + eventDisciplinePartId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DisciplinePartPersonSportKey &&
                ((DisciplinePartPersonSportKey)obj).eventPartPersonSportKey.equals(eventPartPersonSportKey) &&
                ((DisciplinePartPersonSportKey)obj).eventDisciplinePartId == eventDisciplinePartId;
    }

    @Override
    public String getSepValues(String delim) {
        return eventPartPersonSportKey.getSepValues(delim) + delim + eventDisciplinePartId;
    }

    @Override
    public String getWhereClause() {
        return eventPartPersonSportKey.getWhereClause() + " AND eventdisciplinepartid = " + eventDisciplinePartId;
    }

    @Override
    public EventPartPersonSportKey getSuperKey() {
        return eventPartPersonSportKey;
    }

    public int getEventDisciplinePartId() {
        return eventDisciplinePartId;
    }
}
