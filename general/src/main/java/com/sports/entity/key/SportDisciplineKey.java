package com.sports.entity.key;

public class SportDisciplineKey extends SuperKey {
    private final int sportId;
    private final int sportDisciplineId;

    public SportDisciplineKey(int sportId, int sportDisciplineId) {
        this.sportId = sportId;
        this.sportDisciplineId = sportDisciplineId;
    }

    @Override
    public int hashCode() {
        return 100 * sportId + sportDisciplineId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SportDisciplineKey
                && ((SportDisciplineKey)obj).sportId == sportId
                && ((SportDisciplineKey)obj).sportDisciplineId == sportDisciplineId;
    }

    public String getWhereClause() {
        return "sportid = " + sportId + " AND sportdisciplineid = " + sportDisciplineId;
    }

    public String getSepValues(String delim) {
        return sportId + delim + sportDisciplineId;
    }

    public int getSportId() {
        return sportId;
    }

    public int getSportDisciplineId() {
        return sportDisciplineId;
    }
}
