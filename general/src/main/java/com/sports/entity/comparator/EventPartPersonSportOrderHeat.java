package com.sports.entity.comparator;

import com.sports.entity.EventPartPersonSport;

public class EventPartPersonSportOrderHeat extends SuperComparator<EventPartPersonSport> {
    public int compare(EventPartPersonSport o1, EventPartPersonSport o2) {
        if (o1.getSportEventPartOrder() == o2.getSportEventPartOrder()) {
            if (o1.getHeat().equals(o2.getHeat()))
                return o1.getPersonSportId() - o2.getPersonSportId();

            return o1.getHeat() - o2.getHeat();
        }

        return o1.getSportEventPartOrder() - o2.getSportEventPartOrder();
    }
}
