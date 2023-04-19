package com.sports.entity.comparator;

import com.sports.entity.Season;

public class SeasonId extends SuperComparator<Season> {
    public int compare(Season o1, Season o2) {
        return o1.getId() - o2.getId();
    }
}
