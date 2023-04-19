package com.sports.entity.comparator;

import com.sports.entity.CompSeasonPhase;

public class CompSeasonPhaseRoundDescription extends SuperComparator<CompSeasonPhase> {
    public int compare(CompSeasonPhase o1, CompSeasonPhase o2) {
        int roundCompare = compareIntegers(o1.getRound(), o2.getRound());

        if (roundCompare == 0)
            return compareStrings(o1.getDescription(), o2.getDescription());

        return roundCompare;
    }
}
