package com.sports.entity.comparator;

import com.sports.entity.Aliasable;

public class AliasableName extends SuperComparator<Aliasable> {
    public int compare(Aliasable o1, Aliasable o2) {
        return compareStrings(o1.getName(), o2.getName());
    }
}
