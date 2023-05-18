package com.sports.entity.comparator;

import com.sports.entity.Participant;

public class ParticipantResPoints extends SuperComparator<Participant> {
    @Override
    public int compare(Participant o1, Participant o2) {
        if (o1.getResultPoints() == o2.getResultPoints())
            return 0;

        return o1.getResultPoints() < o2.getResultPoints() ? -1 : 1;
    }
}
