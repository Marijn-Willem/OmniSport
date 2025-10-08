package com.sports.entity.comparator;

import com.sports.entity.EventPersonSport;

public class EventPersonSportRankDescription extends SuperComparator<EventPersonSport> {
    @Override
    public int compare(EventPersonSport o1, EventPersonSport o2) {
        int rankCompare = compareIntegers(o1.getRank(), o2.getRank());

        if (rankCompare == 0)
            return compareStrings(o1.getDescription(), o2.getDescription());

        return rankCompare;
    }
}
