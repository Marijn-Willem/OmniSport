package com.sports.entity.comparator;

import com.sports.entity.EventPartPersonSport;

public class EventPartPersonSportEventDate extends SuperComparator<EventPartPersonSport> {
    @Override
    public int compare(EventPartPersonSport o1, EventPartPersonSport o2) {
        return compareLocalDateTimes(o1.getEventDate(), o2.getEventDate());
    }
}
