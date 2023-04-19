package com.sports.entity.comparator;

import com.sports.entity.DescribedEntity;

public class DescribedEntityDescription extends SuperComparator<DescribedEntity> {
    @Override
    public int compare(DescribedEntity o1, DescribedEntity o2) {
        return compareStrings(o1.getDescription(), o2.getDescription());
    }
}
