package com.sports.entity.comparator;

import com.sports.entity.Orderable;

public class OrderableOrder extends SuperComparator<Orderable> {
    public int compare(Orderable o1, Orderable o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
