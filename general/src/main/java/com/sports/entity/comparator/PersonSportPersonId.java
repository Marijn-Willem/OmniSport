package com.sports.entity.comparator;

import com.sports.entity.PersonSport;

public class PersonSportPersonId extends SuperComparator<PersonSport> {
    public int compare(PersonSport o1, PersonSport o2) {
        return o1.getPersonId() - o2.getPersonId();
    }
}
