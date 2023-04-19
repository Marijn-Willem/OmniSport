package com.sports.entity.comparator;

import com.sports.entity.EventDisciplinePart;

public class EventDisciplinePartOrder extends SuperComparator<EventDisciplinePart> {
    public int compare(EventDisciplinePart o1, EventDisciplinePart o2) {
        return o1.getDisciplinePart().getOrder() - o2.getDisciplinePart().getOrder();
    }
}
