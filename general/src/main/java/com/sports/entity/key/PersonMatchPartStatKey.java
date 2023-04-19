package com.sports.entity.key;

public class PersonMatchPartStatKey extends H2HMatchPartStatKey {
    public PersonMatchPartStatKey(PersonMatchPartKey pmpk, int personMatchPartStatId) {
        super(pmpk, personMatchPartStatId);
    }

    String getSpecificIdName() {
        return "personmatchpartstatid";
    }
}
