package com.sports.entity.comparator;

import com.sports.entity.SportEventPart;

public class SportEventPartOrder extends SuperComparator<SportEventPart> {
    public int compare(SportEventPart o1, SportEventPart o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
