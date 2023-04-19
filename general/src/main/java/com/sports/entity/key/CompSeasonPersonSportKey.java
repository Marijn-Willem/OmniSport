package com.sports.entity.key;

public class CompSeasonPersonSportKey extends CompSeasonParticipantKey {
    public CompSeasonPersonSportKey(CompSeasonKey compSeasonKey, int specificId) {
        super(compSeasonKey, specificId);
    }

    String getSpecificIdName() {
        return "personsportid";
    }
}
