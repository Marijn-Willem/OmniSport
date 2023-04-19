package com.sports.entity.comparator;

import com.sports.entity.AlcifoPartParticipant;

public class AlcifoPartParticipantPoints extends SuperComparator<AlcifoPartParticipant> {
    @Override
    public int compare(AlcifoPartParticipant o1, AlcifoPartParticipant o2) {
        return compareIntegers(o1.getPoints(), o2.getPoints());
    }
}
