package com.sports.entity.comparator;

import com.sports.entity.DisciplinePart;

public class DisciplinePartSportDisciplineId extends SuperComparator<DisciplinePart> {
    public int compare(DisciplinePart o1, DisciplinePart o2) {
        return o1.getSportDisciplineId() - o2.getSportDisciplineId();
    }
}
