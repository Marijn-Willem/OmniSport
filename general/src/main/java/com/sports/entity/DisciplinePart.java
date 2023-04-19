package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class DisciplinePart extends SuperKeyAliasable implements Orderable {
    private String name;
    private int order;

    private int sportDisciplineId;
    private int disciplinePartId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                "" + order
            };
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdDisciplinePart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSportDisciplineId() {
        return sportDisciplineId;
    }

    public void setSportDisciplineId(int sportDisciplineId) {
        this.sportDisciplineId = sportDisciplineId;
    }

    public int getDisciplinePartId() {
        return disciplinePartId;
    }

    public void setDisciplinePartId(int disciplinePartId) {
        this.disciplinePartId = disciplinePartId;
    }
}
