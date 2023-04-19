package com.sports.entity.comparator;

import com.sports.entity.Team;

public class TeamCompDivisionSort extends SuperComparator<Team> {
    @Override
    public int compare(Team o1, Team o2) {
        if (o1.getCompDivisionSort() == o2.getCompDivisionSort())
            return compareStrings(o1.getDescription(), o2.getDescription());

        return o1.getCompDivisionSort() - o2.getCompDivisionSort();
    }
}
