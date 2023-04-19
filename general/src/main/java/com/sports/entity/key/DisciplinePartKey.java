package com.sports.entity.key;

public class DisciplinePartKey extends SuperKey {
    private final SportDisciplineKey sportDisciplineKey;
    private final int disciplinePartId;

    public DisciplinePartKey(SportDisciplineKey sportDisciplineKey, int disciplinePartId) {
        this.sportDisciplineKey = sportDisciplineKey;
        this.disciplinePartId = disciplinePartId;
    }

    @Override
    public int hashCode() {
        return 100 * sportDisciplineKey.hashCode() + disciplinePartId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DisciplinePartKey &&
                ((DisciplinePartKey)obj).sportDisciplineKey.equals(sportDisciplineKey) &&
                ((DisciplinePartKey)obj).disciplinePartId == disciplinePartId;
    }

    @Override
    public String getWhereClause() {
        return sportDisciplineKey.getWhereClause() + " AND disciplinepartid = " + disciplinePartId;
    }

    @Override
    public String getSepValues(String delim) {
        return sportDisciplineKey.getSepValues(delim) + delim + disciplinePartId;
    }

    @Override
    public SportDisciplineKey getSuperKey() {
        return sportDisciplineKey;
    }

    public int getDisciplinePartId() {
        return disciplinePartId;
    }
}
