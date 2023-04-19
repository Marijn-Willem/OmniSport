package com.sports.entity.comparator;

import com.sports.entity.EventPartPersonSport;

public class EventPartPersonSportStage extends SuperComparator<EventPartPersonSport> {
    @Override
    public int compare(EventPartPersonSport o1, EventPartPersonSport o2) {
        return compareIntegers(o1.getCompSeasonEventPart().getStage(), o2.getCompSeasonEventPart().getStage());
    }
}
