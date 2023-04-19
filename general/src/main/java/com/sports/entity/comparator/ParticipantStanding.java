package com.sports.entity.comparator;

import com.sports.entity.Participant;

public class ParticipantStanding<T extends Participant> extends SuperComparator<T> {
    public int compare(T o1, T o2) {
        int pointsCompare = compareIntegers(o2.getPoints(), o1.getPoints());

        if (pointsCompare == 0) {
            if (o1.getScoreDiff() == o2.getScoreDiff())
                return o2.getScore() - o1.getScore();

            return o2.getScoreDiff() - o1.getScoreDiff();
        }

        return pointsCompare;
    }
}
