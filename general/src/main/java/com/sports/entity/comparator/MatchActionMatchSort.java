package com.sports.entity.comparator;

import com.sports.entity.TeamMatchAction;

public class MatchActionMatchSort extends SuperComparator<TeamMatchAction> {
    @Override
    public int compare(TeamMatchAction o1, TeamMatchAction o2) {
        return o1.getMatchSort() - o2.getMatchSort();
    }
}
