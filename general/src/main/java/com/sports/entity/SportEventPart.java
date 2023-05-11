package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class SportEventPart extends SuperKeyAliasable implements Orderable {
    private int sportDisciplineId;
    private String name;
    private int order;
    private Integer weight;

    private int sportEventPartId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(sportDisciplineId),
                QueryUtil.convertStringToDbValue(name),
                String.valueOf(order),
                QueryUtil.convertIntegerToDbValue(weight)
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

    public int getSportEventPartId() {
        return sportEventPartId;
    }

    public void setSportEventPartId(int sportEventPartId) {
        this.sportEventPartId = sportEventPartId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
