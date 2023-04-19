package com.sports.entity;

import java.util.HashSet;
import java.util.Set;

public class EventPersonSport extends AlcifoParticipant {
    private final Set<Integer> teamIds = new HashSet<>();

    public void addTeamId(int teamId) {
        teamIds.add(teamId);
    }

    public Integer getSingleTeamId() {
        Integer singleTeamId = null;

        if (teamIds.size() == 1)
            for (Integer teamId : teamIds)
                singleTeamId = teamId;

        return singleTeamId;
    }
}

