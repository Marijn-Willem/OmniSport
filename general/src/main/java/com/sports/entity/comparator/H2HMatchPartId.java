package com.sports.entity.comparator;

import com.sports.entity.H2HMatchPart;

public class H2HMatchPartId extends SuperComparator<H2HMatchPart> {
    public int compare(H2HMatchPart o1, H2HMatchPart o2) {
        return o1.getMatchPartId() - o2.getMatchPartId();
    }
}
