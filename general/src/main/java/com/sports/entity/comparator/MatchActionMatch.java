package com.sports.entity.comparator;

import com.sports.entity.TeamMatchAction;

public class MatchActionMatch extends SuperComparator<TeamMatchAction> {
    public int compare(TeamMatchAction o1, TeamMatchAction o2) {
        return o1.getTeamMatchId() - o2.getTeamMatchId();
    }
}
