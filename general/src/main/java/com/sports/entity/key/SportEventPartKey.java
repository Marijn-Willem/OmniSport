package com.sports.entity.key;

public class SportEventPartKey extends SuperKey {
    private SportEventKey sportEventKey;
    private int sportEventPartId;

    public SportEventPartKey(SportEventKey sek, int sportEventPartId) {
        this.sportEventKey = sek;
        this.sportEventPartId = sportEventPartId;
    }

    @Override
    public int hashCode() {
        return 100 * sportEventKey.hashCode() + sportEventPartId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SportEventPartKey &&
                ((SportEventPartKey)obj).sportEventKey.equals(sportEventKey) &&
                ((SportEventPartKey)obj).getSportEventPartId() == sportEventPartId;
    }

    @Override
    public String getSepValues(String delim) {
        return sportEventKey.getSepValues(delim) + delim + sportEventPartId;
    }

    @Override
    public String getWhereClause() {
        return sportEventKey.getWhereClause() + " AND sporteventpartid = " + sportEventPartId;
    }

    @Override
    public SportEventKey getSuperKey() {
        return sportEventKey;
    }

    public int getSportEventPartId() {
        return sportEventPartId;
    }
}
