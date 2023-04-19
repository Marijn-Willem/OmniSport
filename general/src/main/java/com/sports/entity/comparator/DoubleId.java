package com.sports.entity.comparator;

import com.sports.entity.Double;

public class DoubleId extends SuperComparator<Double> {
    public int compare(Double o1, Double o2) {
        return o1.getId() - o2.getId();
    }
}
