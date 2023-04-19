package com.sports.entity.key;

public class CompSeasonPhasePersonSportKey extends CompSeasonPhaseParticipantKey {
    public CompSeasonPhasePersonSportKey(CompSeasonPhaseKey compSeasonPhaseKey, int specificId) {
        super(compSeasonPhaseKey, specificId);
    }

    String getSpecificIdName() {
        return "personsportid";
    }

    public CompSeasonPersonSportKey getCompSeasonPersonSportKey() {
        return new CompSeasonPersonSportKey(getSuperKey().getSuperKey(), getSpecificId());
    }
}
