package com.sports.entity.comparator;

import com.sports.entity.H2HMatch;

public class H2HMatchPhaseRoundKnockoutOrder extends SuperComparator<H2HMatch> {
    public int compare(H2HMatch o1, H2HMatch o2) {
        int comparePhaseRound = compareIntegers(o1.getPhaseRound(), o2.getPhaseRound());

        if (comparePhaseRound == 0) {
            int compareKnockoutOrder = compareIntegers(o1.getKnockoutOrder(), o2.getKnockoutOrder());

            if (compareKnockoutOrder == 0)
                return compareLocalDateTimes(o1.getDate(), o2.getDate());

            return compareKnockoutOrder;
        }

        return comparePhaseRound;
    }
}
