package com.sports.entity.comparator;

import com.sports.entity.EntityInstance;

public class EntityInstanceStartDate extends SuperComparator<EntityInstance> {
    @Override
    public int compare(EntityInstance o1, EntityInstance o2) {
        return compareLocalDateTimes(o1.getEndDate(), o2.getEndDate());
    }
}
