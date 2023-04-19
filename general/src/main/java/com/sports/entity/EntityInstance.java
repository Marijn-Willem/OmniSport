package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.time.LocalDateTime;

public abstract class EntityInstance extends SuperKeyAliasable {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int entityId;
    private int entityInstanceId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertDateTimeToDbString(startDate),
                QueryUtil.convertDateTimeToDbString(endDate)
        };
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityInstanceId() {
        return entityInstanceId;
    }

    public void setEntityInstanceId(int entityInstanceId) {
        this.entityInstanceId = entityInstanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
