package com.sports.entity;

import com.sports.db.util.QueryUtil;

public abstract class H2HMatchPart extends SuperKeyEntity {
    private String name;
    private Integer parentMatchPartId;
    private boolean finished;

    public abstract int getMatchPartId();
    abstract String[] getSpecificPropertiesInSQLStrings();
    public abstract boolean getParticipant1Win();
    public abstract void setParticipant1Win(boolean participant1Win);

    @Override
    public String[] getPropertiesInSQLStrings() {
        String[] specifProps = getSpecificPropertiesInSQLStrings();
        String[] sqlProps = new String[specifProps.length + 3];

        System.arraycopy(specifProps, 0, sqlProps, 0, specifProps.length);
        sqlProps[specifProps.length] = QueryUtil.convertStringToDbValue(name);
        sqlProps[specifProps.length + 1] = QueryUtil.convertIntegerToDbValue(parentMatchPartId);
        sqlProps[specifProps.length + 2] = QueryUtil.convertBooleanToDbValue(finished);

        return sqlProps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentMatchPartId() {
        return parentMatchPartId;
    }

    public void setParentMatchPartId(Integer parentMatchPartId) {
        this.parentMatchPartId = parentMatchPartId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
