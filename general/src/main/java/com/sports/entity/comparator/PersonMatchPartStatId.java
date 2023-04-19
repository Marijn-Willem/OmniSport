package com.sports.entity.comparator;

import com.sports.entity.PersonMatchPartStat;

public class PersonMatchPartStatId extends SuperComparator<PersonMatchPartStat> {
    public int compare(PersonMatchPartStat o1, PersonMatchPartStat o2) {
        if (o1.getPersonMatchPartId() == o2.getPersonMatchPartId())
            return o1.getPersonMatchPartStatId() - o2.getPersonMatchPartStatId();

        return o1.getPersonMatchPartId() - o2.getPersonMatchPartId();
    }
}
