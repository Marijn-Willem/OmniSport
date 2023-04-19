package com.sports.entity.comparator;

import com.sports.entity.Season;

public class SeasonOrderDesc extends SuperComparator<Season> {
    public int compare(Season o1, Season o2) {
        return o2.getOrder() - o1.getOrder();
    }
}
