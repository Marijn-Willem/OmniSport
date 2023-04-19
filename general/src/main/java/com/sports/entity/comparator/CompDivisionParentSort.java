package com.sports.entity.comparator;

import com.sports.entity.CompDivision;

public class CompDivisionParentSort extends SuperComparator<CompDivision> {
    @Override
    public int compare(CompDivision o1, CompDivision o2) {
        if (o1.getParentDivisionSort() == o2.getParentDivisionSort())
            return compareStrings(o1.getName(), o2.getName());

        return o1.getParentDivisionSort() - o2.getParentDivisionSort();
    }
}
