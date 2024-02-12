package com.sports.entity.comparator;

import com.sports.entity.H2HMatch;

public class H2HMatchKnockoutOrder extends SuperComparator<H2HMatch> {
    public int compare(H2HMatch o1, H2HMatch o2) {
        return compareIntegers(o1.getKnockoutOrder(), o2.getKnockoutOrder());
    }
}
