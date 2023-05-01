package com.sports.entity.key;

public class SportEventKey extends SuperKey {
    private final int sportId;
    private final int sportEventId;

    public SportEventKey(int sportId, int sportEventId) {
        this.sportId = sportId;
        this.sportEventId = sportEventId;
    }

    @Override
    public int hashCode() {
        return 100 * sportId + sportEventId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof  SportEventKey
            && ((SportEventKey)obj).getSportId() == sportId
            && ((SportEventKey)obj).getSportEventId() == sportEventId;
    }

    @Override
    public String getWhereClause() {
        return "sportid = " + sportId + " AND sporteventid = " + sportEventId;
    }

    public String getSepValues(String delim) {
        return sportId + delim + sportEventId;
    }

    public int getSportId() {
        return sportId;
    }

    public int getSportEventId() {
        return sportEventId;
    }
}
