package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class PhaseType extends IntAliasable {
    private int id;
    private String name;
    private Integer parentId;
    private boolean isParent;

    @Override
    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdPhaseType;
    }

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertIntegerToDbValue(parentId),
                QueryUtil.convertBooleanToDbValue(isParent)
        };
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }
}
