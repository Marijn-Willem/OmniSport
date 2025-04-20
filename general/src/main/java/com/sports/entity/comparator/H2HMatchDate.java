package com.sports.entity.comparator;

import com.sports.entity.H2HMatch;

public class H2HMatchDate extends SuperComparator<H2HMatch> {
    @Override
    public int compare(H2HMatch o1, H2HMatch o2) {
        return compareLocalDateTimes(o1.getDate(), o2.getDate());
    }
}
