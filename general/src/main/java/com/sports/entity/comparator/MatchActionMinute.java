package com.sports.entity.comparator;

import com.sports.entity.TeamMatchAction;

public class MatchActionMinute extends SuperComparator<TeamMatchAction> {
    public int compare(TeamMatchAction o1, TeamMatchAction o2) {
        return compareIntegers(o1.getMinute(), o2.getMinute());
    }
}
