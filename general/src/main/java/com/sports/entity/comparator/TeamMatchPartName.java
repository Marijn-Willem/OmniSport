package com.sports.entity.comparator;

import com.sports.entity.TeamMatchPart;

public class TeamMatchPartName extends SuperComparator<TeamMatchPart> {
    @Override
    public int compare(TeamMatchPart o1, TeamMatchPart o2) {
        return compareStrings(o1.getName(), o2.getName());
    }
}
