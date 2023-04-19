package com.sports.entity.comparator;

import com.sports.entity.EventPartPersonSport;

public class EventPartPersonPersonId extends SuperComparator<EventPartPersonSport> {
    public int compare(EventPartPersonSport o1, EventPartPersonSport o2) {
        if (o1.getPersonSportId() == o2.getPersonSportId())
            return o1.getCompSeasonEventPartId() - o2.getCompSeasonEventPartId();

        return o1.getPersonSportId() - o2.getPersonSportId();
    }
}
