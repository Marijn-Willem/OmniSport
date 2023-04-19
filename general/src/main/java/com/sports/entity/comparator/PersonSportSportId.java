package com.sports.entity.comparator;

import com.sports.entity.PersonSport;

public class PersonSportSportId extends SuperComparator<PersonSport> {
    public int compare(PersonSport o1, PersonSport o2) {
        return o1.getSportId() - o2.getSportId();
    }
}
