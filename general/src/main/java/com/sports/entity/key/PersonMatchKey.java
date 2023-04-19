package com.sports.entity.key;

public class PersonMatchKey extends H2HMatchKey {
    public PersonMatchKey(CompSeasonKey csk, int personMatchId) {
        super(csk, personMatchId);
    }

    String getSpecificIdName() {
        return "personmatchid";
    }

    public int getPersonMatchId() {
        return getSpecificId();
    }
}
