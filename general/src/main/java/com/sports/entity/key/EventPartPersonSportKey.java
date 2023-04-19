package com.sports.entity.key;

public class EventPartPersonSportKey extends SuperKey {
    private final CompSeasonEventPartKey compSeasonEventPartKey;
    private final int personSportId;

    public EventPartPersonSportKey(CompSeasonEventPartKey csepk, int personSportId) {
        this.compSeasonEventPartKey = csepk;
        this.personSportId = personSportId;
    }

    @Override
    public int hashCode() {
        return 10000 * compSeasonEventPartKey.hashCode() + personSportId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EventPartPersonSportKey &&
                ((EventPartPersonSportKey)obj).compSeasonEventPartKey.equals(compSeasonEventPartKey) &&
                ((EventPartPersonSportKey)obj).getPersonSportId() == personSportId;
    }

    @Override
    public String getSepValues(String delim) {
        return compSeasonEventPartKey.getSepValues(delim) + delim + personSportId;
    }

    @Override
    public String getWhereClause() {
        return compSeasonEventPartKey.getWhereClause() + " AND personsportid = " + personSportId;
    }

    @Override
    public CompSeasonEventPartKey getSuperKey() {
       return compSeasonEventPartKey;
    }

    public int getPersonSportId() {
        return personSportId;
    }
}
