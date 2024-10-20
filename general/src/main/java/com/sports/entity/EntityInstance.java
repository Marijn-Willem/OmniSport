package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.logic.util.Util;

import java.time.LocalDateTime;

public abstract class EntityInstance extends SuperKeyAliasable {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int entityId;
    private int entityInstanceId;

    abstract String[] getSpecificPropertiesInSQLStrings();

    @Override
    public String[] getPropertiesInSQLStrings() {
        String[] generalProps = {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertDateTimeToDbString(startDate),
                QueryUtil.convertDateTimeToDbString(endDate)
        };

        return Util.concatenateStringArrays(generalProps, getSpecificPropertiesInSQLStrings());
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
