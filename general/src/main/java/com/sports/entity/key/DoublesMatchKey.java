package com.sports.entity.key;

public class DoublesMatchKey extends H2HMatchKey {
    public DoublesMatchKey(CompSeasonKey compSeasonKey, int doublesMatchId) {
        super(compSeasonKey, doublesMatchId);
    }

    String getSpecificIdName() {
        return "doublesmatchid";
    }
}
