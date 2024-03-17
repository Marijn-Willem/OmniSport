package com.sports.entity.comparator;

import com.sports.entity.H2HMatch;

public class H2HMatchKnockoutOrderDate extends SuperComparator<H2HMatch> {
    public int compare(H2HMatch o1, H2HMatch o2) {
        int compare = compareIntegers(o1.getKnockoutOrder(), o2.getKnockoutOrder());

        if (compare == 0)
            compare = compareLocalDateTimes(o1.getDate(), o2.getDate());

        return compare;
    }
}
