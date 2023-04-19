package com.sports.entity.key;

public class DoublesMatchPartKey extends H2HMatchPartKey {
    public DoublesMatchPartKey(DoublesMatchKey doublesMatchKey, int doublesMatchPartId) {
        super(doublesMatchKey, doublesMatchPartId);
    }

    String getSpecificIdName() {
        return "doublesmatchpartid";
    }
}
