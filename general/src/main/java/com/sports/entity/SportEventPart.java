package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class SportEventPart extends SuperKeyAliasable implements Orderable {
    private int sportDisciplineId;
    private String name;
    private int order;
    private Integer weight;
    private boolean isFinal;

    private int sportEventPartId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(sportDisciplineId),
                QueryUtil.convertStringToDbValue(name),
                String.valueOf(order),
                QueryUtil.convertIntegerToDbValue(weight),
                QueryUtil.convertBooleanToDbValue(isFinal)
            };
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdSportEventPart;
    }

    public int getSportDisciplineId() {
        return sportDisciplineId;
    }

    public void setSportDisciplineId(int sportDisciplineId) {
        this.sportDisciplineId = sportDisciplineId;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public int getSportEventPartId() {
        return sportEventPartId;
    }

    public void setSportEventPartId(int sportEventPartId) {
        this.sportEventPartId = sportEventPartId;
    }
}
