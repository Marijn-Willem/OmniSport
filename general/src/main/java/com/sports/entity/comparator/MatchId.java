package com.sports.entity.comparator;

import com.sports.entity.H2HMatch;

public class MatchId extends SuperComparator<H2HMatch> {
    public int compare(H2HMatch o1, H2HMatch o2) {
        return o1.getSpecificId() - o2.getSpecificId();
    }
}
