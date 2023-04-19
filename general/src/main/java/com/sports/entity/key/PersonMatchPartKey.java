package com.sports.entity.key;

public class PersonMatchPartKey extends H2HMatchPartKey {
    public PersonMatchPartKey(PersonMatchKey pmk, int personMatchPartId) {
        super(pmk, personMatchPartId);
    }

    public int getPersonMatchId() {
        return getSuperKey().getSpecificId();
    }

    String getSpecificIdName() {
        return "personmatchpartid";
    }
}
