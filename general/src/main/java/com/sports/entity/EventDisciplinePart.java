package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class EventDisciplinePart extends SuperKeyEntity implements NamedEntity {
    private int sportDisciplineId;
    private Integer disciplinePartId;
    private String name;

    private int eventDisciplinePartId;
    private DisciplinePart disciplinePart;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(sportDisciplineId),
                QueryUtil.convertIntegerToDbValue(disciplinePartId),
                QueryUtil.convertStringToDbValue(name)
        };
    }

    public int getSportDisciplineId() {
        return sportDisciplineId;
    }

    public void setSportDisciplineId(int sportDisciplineId) {
        this.sportDisciplineId = sportDisciplineId;
    }

    public Integer getDisciplinePartId() {
        return disciplinePartId;
    }

    public void setDisciplinePartId(Integer disciplinePartId) {
        this.disciplinePartId = disciplinePartId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEventDisciplinePartId() {
        return eventDisciplinePartId;
    }

    public void setEventDisciplinePartId(int eventDisciplinePartId) {
        this.eventDisciplinePartId = eventDisciplinePartId;
    }

    public DisciplinePart getDisciplinePart() {
        return disciplinePart;
    }

    public void setDisciplinePart(DisciplinePart disciplinePart) {
        this.disciplinePart = disciplinePart;
    }
}
