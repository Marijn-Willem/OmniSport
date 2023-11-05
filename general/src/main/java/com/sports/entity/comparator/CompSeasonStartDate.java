package com.sports.entity.comparator;

import com.sports.entity.CompSeason;

public class CompSeasonStartDate extends SuperComparator<CompSeason> {
    @Override
    public int compare(CompSeason o1, CompSeason o2) {
        return compareLocalDateTimes(o1.getStartDate(), o2.getStartDate());
    }
}
