package com.sports.entity.key;

public class DoublePersonSport1PersonSport2Key extends SuperKey {
    private final int personSport1Id;
    private final int personSport2Id;

    public DoublePersonSport1PersonSport2Key(int personSport1Id, int personSport2Id) {
        this.personSport1Id = personSport1Id;
        this.personSport2Id = personSport2Id;
    }

    @Override
    public int hashCode() {
        return 1000 * personSport1Id + personSport2Id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DoublePersonSport1PersonSport2Key &&
                ((DoublePersonSport1PersonSport2Key)obj).personSport1Id == personSport1Id &&
                ((DoublePersonSport1PersonSport2Key)obj).personSport2Id == personSport2Id;
    }

    @Override
    public String getWhereClause() {
        return "personsport1id = " + personSport1Id + " AND personsport2id = " + personSport2Id;
    }

    @Override
    public String getSepValues(String delim) {
        return personSport1Id + delim + personSport2Id;
    }

    public int getPersonSport1Id() {
        return personSport1Id;
    }

    public int getPersonSport2Id() {
        return personSport2Id;
    }
}
