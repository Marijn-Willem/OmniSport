package com.sports.entity.key;

public class PersonSportIdKey extends SuperKey {
    private int personId;
    private int sportId;

    public PersonSportIdKey(int personId, int sportId) {
        this.personId = personId;
        this.sportId = sportId;
    }

    @Override
    public int hashCode() {
        return 100 * personId + sportId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PersonSportIdKey &&
                ((PersonSportIdKey)obj).personId == personId &&
                ((PersonSportIdKey)obj).sportId == sportId;
    }

    public String getWhereClause() {
        return "personid = " + personId + " AND sportid = " + sportId;
    }

    public String getSepValues(String delim) {
        return personId + delim + sportId;
    }
}
