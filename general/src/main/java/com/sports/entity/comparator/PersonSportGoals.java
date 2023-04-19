package com.sports.entity.comparator;

import com.sports.entity.PersonSport;

public class PersonSportGoals extends SuperComparator<PersonSport> {
    public int compare(PersonSport o1, PersonSport o2) {
        return o2.getGoals() - o1.getGoals();
    }
}
