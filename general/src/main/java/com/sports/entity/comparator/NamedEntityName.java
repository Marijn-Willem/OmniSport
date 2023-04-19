package com.sports.entity.comparator;

import com.sports.entity.NamedEntity;

public class NamedEntityName extends SuperComparator<NamedEntity> {
    public int compare(NamedEntity o1, NamedEntity o2) {
        return compareStrings(o1.getName(), o2.getName());
    }
}
