package com.sports.entity.comparator;

import com.sports.entity.CompDivision;

public class CompDivisionParentName extends SuperComparator<CompDivision> {
    public int compare(CompDivision o1, CompDivision o2) {
        if (o1.getParentDivisionId() == null && o2.getParentDivisionId() != null)
            return -1;

        if (o2.getParentDivisionId() == null && o1.getParentDivisionId() != null)
            return 1;

        return compareStrings(o1.getName(), o2.getName());
    }
}
