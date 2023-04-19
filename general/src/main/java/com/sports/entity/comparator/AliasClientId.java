package com.sports.entity.comparator;

import com.sports.entity.Alias;

public class AliasClientId extends SuperComparator<Alias> {
    public int compare(Alias o1, Alias o2) {
        return compareIntegers(o1.getClientId(), o2.getClientId());
    }
}
