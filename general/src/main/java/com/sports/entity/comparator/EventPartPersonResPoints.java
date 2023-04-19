package com.sports.entity.comparator;

import com.sports.entity.EventPartPersonSport;

public class EventPartPersonResPoints extends SuperComparator<EventPartPersonSport> {
    public int compare(EventPartPersonSport o1, EventPartPersonSport o2) {
        return (int)(1000.0 * o1.getResultPoints()) - (int)(1000.0 * o2.getResultPoints());
    }
}
