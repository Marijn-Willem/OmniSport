package com.sports.cache.util;

import com.sports.entity.comparator.SuperComparator;

public class CacheListObjectComparator extends SuperComparator<CacheListObject> {
    @Override
    public int compare(CacheListObject o1, CacheListObject o2) {
        return compareStrings(o1.key(), o2.key());
    }
}
