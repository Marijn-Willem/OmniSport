package com.sports.entity.comparator;

import com.sports.entity.H2HMatch;

public class MatchDate extends SuperComparator<H2HMatch> {
    public int compare(H2HMatch o1, H2HMatch o2) {
        int compareDate = compareLocalDateTimes(o1.getDate(), o2.getDate());

        if (compareDate == 0)
            return compareIntegers(o1.getKnockoutOrder(), o2.getKnockoutOrder());

        return compareDate;
    }
}
