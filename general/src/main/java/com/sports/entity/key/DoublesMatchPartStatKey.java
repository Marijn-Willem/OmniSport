package com.sports.entity.key;

public class DoublesMatchPartStatKey extends H2HMatchPartStatKey {
    public DoublesMatchPartStatKey(DoublesMatchPartKey doublesMatchPartKey, int doublesMatchPartStatId) {
        super(doublesMatchPartKey, doublesMatchPartStatId);
    }

    String getSpecificIdName() {
        return "doublesmatchpartstatid";
    }
}
