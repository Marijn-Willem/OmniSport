package com.sports.entity.comparator;

import com.sports.entity.Alias;

public class AliasListDisplayText extends SuperComparator<Alias> {
    @Override
    public int compare(Alias o1, Alias o2) {
        return compareStrings(o1.getListDisplayText(), o2.getListDisplayText());
    }
}
