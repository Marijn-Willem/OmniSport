package com.sports.entity.comparator;

import com.sports.entity.CompSeasonPhase;

public class CompSeasonPhaseParentOrder extends SuperComparator<CompSeasonPhase> {
    public int compare(CompSeasonPhase o1, CompSeasonPhase o2) {
        return compareIntegers(o1.getParentOrder(), o2.getParentOrder());
    }
}
