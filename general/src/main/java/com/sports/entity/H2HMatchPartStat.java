package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.logic.util.Util;

public abstract class H2HMatchPartStat extends SuperKeyEntity {
    private int statTypeId;
    private Integer value;

    public abstract int getMatchPartId();
    public abstract int getMatchPartStatId();
    public abstract void setParticipantId(int participantId);
    abstract String[] getSpecificProperties();
    abstract void copySpecific(H2HMatchPartStat other);

    @Override
    public String[] getPropertiesInSQLStrings() {
        String[] genericProperties = {
                "" + statTypeId,
                QueryUtil.convertIntegerToDbValue(value)
        };

        return Util.concatenateStringArrays(getSpecificProperties(), genericProperties);
    }

    public int getStatTypeId() {
        return statTypeId;
    }

    public void setStatTypeId(int statTypeId) {
        this.statTypeId = statTypeId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void copy(H2HMatchPartStat other) {
        other.statTypeId = this.statTypeId;
        other.value = this.value;

        copySpecific(other);
    }
}
