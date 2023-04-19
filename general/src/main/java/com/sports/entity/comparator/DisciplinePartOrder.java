package com.sports.entity.comparator;

import com.sports.entity.DisciplinePart;

public class DisciplinePartOrder extends SuperComparator<DisciplinePart> {
    public int compare(DisciplinePart o1, DisciplinePart o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
