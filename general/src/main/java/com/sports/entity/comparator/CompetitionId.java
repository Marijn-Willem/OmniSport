package com.sports.entity.comparator;

import com.sports.entity.Competition;

public class CompetitionId extends SuperComparator<Competition> {
    public int compare(Competition o1, Competition o2) {
        return o1.getId() - o2.getId();
    }
}
