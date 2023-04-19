package com.sports.entity.key;

public class CompSeasonPhaseDoubleKey extends CompSeasonPhaseParticipantKey {
    public CompSeasonPhaseDoubleKey(CompSeasonPhaseKey compSeasonPhaseKey, int compSeasonDoubleId) {
        super(compSeasonPhaseKey, compSeasonDoubleId);
    }

    String getSpecificIdName() {
        return "doubleid";
    }
}
