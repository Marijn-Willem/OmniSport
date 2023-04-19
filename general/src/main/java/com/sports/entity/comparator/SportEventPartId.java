package com.sports.entity.comparator;

import com.sports.entity.SportEventPart;

public class SportEventPartId extends SuperComparator<SportEventPart> {
    public int compare(SportEventPart o1, SportEventPart o2) {
        return o1.getSportEventPartId() - o2.getSportEventPartId();
    }
}
