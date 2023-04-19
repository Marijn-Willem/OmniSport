package com.sports.entity.comparator;

import com.sports.entity.H2HMatchPartStat;

public class H2HMatchPartStatId extends SuperComparator<H2HMatchPartStat> {
    public int compare(H2HMatchPartStat o1, H2HMatchPartStat o2) {
        if (o1.getMatchPartId() == o2.getMatchPartId())
            return o1.getMatchPartStatId() - o2.getMatchPartStatId();

        return o1.getMatchPartId() - o2.getMatchPartId();
    }
}
