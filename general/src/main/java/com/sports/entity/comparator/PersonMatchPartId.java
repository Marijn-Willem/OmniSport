package com.sports.entity.comparator;

import com.sports.entity.PersonMatchPart;

public class PersonMatchPartId extends SuperComparator<PersonMatchPart> {
    public int compare(PersonMatchPart o1, PersonMatchPart o2) {
        return o1.getPersonMatchPartId() - o2.getPersonMatchPartId();
    }
}
