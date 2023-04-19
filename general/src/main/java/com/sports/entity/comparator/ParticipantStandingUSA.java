package com.sports.entity.comparator;

import com.sports.entity.Team;
import com.sports.logic.calculation.Calculation;

public class ParticipantStandingUSA extends SuperComparator<Team> {
    @Override
    public int compare(Team o1, Team o2) {
        int compare = compareAll(o1, o2);

        if (compare == 0 && o1.getCompDivision() != null && o2.getCompDivision() != null) {
            int compareParentDivision = compareParentDivision(o1, o2);
            int compareDivision = compareDivision(o1, o2);

            if (o1.getCompDivision().getCompDivisionId() == o2.getCompDivision().getCompDivisionId()) {
                compare = compareDivision;

                if (compare == 0)
                    compare = compareParentDivision;
            } else if (compareIntegers(o1.getCompDivision().getParentDivisionId(),
                    o2.getCompDivision().getParentDivisionId()) == 0)
                compare = compareParentDivision;
        }

        if (compare == 0)
            compare = compareStrings(o1.getDescription(), o2.getDescription());

        return compare;
    }

    private int compareAll(Team t1, Team t2) {
        return compare(t1.getPlayed(), t1.getWins(), t1.getDraws(), t2.getPlayed(), t2.getWins(), t2.getDraws());
    }

    private int compareParentDivision(Team t1, Team t2) {
        return compare(t1.getPlayedParentDivision(), t1.getWinsParentDivision(), t1.getDrawsParentDivision(),
                t2.getPlayedParentDivision(), t2.getWinsParentDivision(), t2.getDrawsParentDivision());
    }

    private int compareDivision(Team t1, Team t2) {
        return compare(t1.getPlayedDivision(), t1.getWinsDivision(), t1.getDrawsDivision(),
                t2.getPlayedDivision(), t2.getWinsDivision(), t2.getDrawsDivision());
    }

    private int compare(int played1, int wins1, int draws1, int played2, int wins2, int draws2) {
        double average1 = Calculation.getAverage(played1, wins1, draws1);
        double average2 = Calculation.getAverage(played2, wins2, draws2);

        return (int)Math.signum(average2 - average1);
    }
}
