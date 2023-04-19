package com.sports.entity.comparator;

import com.sports.entity.AlcifoPartParticipant;

public class AlcifoPartParticipantPointsDesc extends SuperComparator<AlcifoPartParticipant> {
    @Override
    public int compare(AlcifoPartParticipant o1, AlcifoPartParticipant o2) {
        return compareIntegers(o2.getPoints(), o1.getPoints());
    }
}
