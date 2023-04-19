package com.sports.entity.comparator;

import com.sports.entity.CompSeasonEventPart;

public class CompSeasonEventPartStage extends SuperComparator<CompSeasonEventPart> {
    @Override
    public int compare(CompSeasonEventPart o1, CompSeasonEventPart o2) {
        return compareIntegers(o1.getStage(), o2.getStage());
    }
}
