package com.sports.entity.comparator;

import com.sports.entity.WithH2HMatchParticipantsDate;

public class H2HMatchParticipantsDate extends SuperComparator<WithH2HMatchParticipantsDate> {
    @Override
    public int compare(WithH2HMatchParticipantsDate o1, WithH2HMatchParticipantsDate o2) {
        int partic1Compare = compareIntegers(o1.getParticipant1Id(), o2.getParticipant1Id());

        if (partic1Compare == 0) {
            int partic2Compare = compareIntegers(o1.getParticipant2Id(), o2.getParticipant2Id());

            if (partic2Compare == 0)
                return o1.getDate().compareTo(o2.getDate());

            return partic2Compare;
        }

        return partic1Compare;
    }
}
