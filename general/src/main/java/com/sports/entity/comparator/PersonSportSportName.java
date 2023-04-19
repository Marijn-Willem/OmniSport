package com.sports.entity.comparator;

import com.sports.entity.PersonSport;

public class PersonSportSportName extends SuperComparator<PersonSport> {
    public int compare(PersonSport o1, PersonSport o2) {
        return compareStrings(o1.getSportName(), o2.getSportName());
    }
}
