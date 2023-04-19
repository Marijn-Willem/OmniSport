package com.sports.entity.key;

public class CompSeasonDoubleKey extends CompSeasonParticipantKey {
    public CompSeasonDoubleKey(CompSeasonKey compSeasonKey, int specificId) {
        super(compSeasonKey, specificId);
    }

    String getSpecificIdName() {
        return "doubleid";
    }

    public int getDoubleId() {
        return getSpecificId();
    }
}
