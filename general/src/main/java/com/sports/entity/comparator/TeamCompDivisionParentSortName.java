package com.sports.entity.comparator;

import com.sports.entity.Team;

public class TeamCompDivisionParentSortName extends SuperComparator<Team> {
    @Override
    public int compare(Team o1, Team o2) {
        int compDivCompare = 0;
        if (o1.getCompDivision() != null && o2.getCompDivision() != null)
            compDivCompare = new CompDivisionParentSort().compare(o1.getCompDivision(), o2.getCompDivision());

        if (compDivCompare == 0)
            return compareStrings(o1.getDescription(), o2.getDescription());

        return compDivCompare;
    }
}
