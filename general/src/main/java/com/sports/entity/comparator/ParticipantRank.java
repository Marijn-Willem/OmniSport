package com.sports.entity.comparator;

import com.sports.entity.Participant;

public class ParticipantRank extends SuperComparator<Participant> {
    @Override
    public int compare(Participant o1, Participant o2) {
        int rankCompare = compareIntegers(o1.getRank(), o2.getRank());

        return rankCompare == 0 ? compareStrings(o1.getDescription(), o2.getDescription()) : rankCompare;
    }
}
