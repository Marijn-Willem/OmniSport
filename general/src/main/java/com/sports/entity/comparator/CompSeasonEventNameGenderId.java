package com.sports.entity.comparator;

import com.sports.entity.CompSeasonEvent;

public class CompSeasonEventNameGenderId extends SuperComparator<CompSeasonEvent> {
    @Override
    public int compare(CompSeasonEvent o1, CompSeasonEvent o2) {
        int eventNameCompare = compareStrings(o1.getSportEventName(), o2.getSportEventName());

        if (eventNameCompare == 0)
            return o1.getGenderId() - o2.getGenderId();

        return eventNameCompare;
    }
}
