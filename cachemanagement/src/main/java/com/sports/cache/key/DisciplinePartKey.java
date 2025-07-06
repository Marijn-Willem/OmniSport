package com.sports.cache.key;

import com.sports.logic.util.Util;

public class DisciplinePartKey extends CacheFragmentKey {
    private final int sportId;
    private final int sportDisciplineId;
    private final int disciplinePartId;

    public DisciplinePartKey(int sportId, int sportDisciplineId, int disciplinePartId) {
        this.sportId = sportId;
        this.sportDisciplineId = sportDisciplineId;
        this.disciplinePartId = disciplinePartId;
    }

    @Override
    String getSpecificKeyPart() {
        return Util.concatStrings(new String[] {
                Integer.toString(sportId), Integer.toString(sportDisciplineId),
                Integer.toString(disciplinePartId)
        }, "|");
    }
}
