package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class CompDivision extends SuperKeyEntity implements NamedEntity {
    private String name;
    private Integer parentDivisionId;

    private int compDivisionId;
    private int parentDivisionSort;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertIntegerToDbValue(parentDivisionId)
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentDivisionId() {
        return parentDivisionId;
    }

    public void setParentDivisionId(Integer parentDivisionId) {
        this.parentDivisionId = parentDivisionId;
    }

    public int getCompDivisionId() {
        return compDivisionId;
    }

    public void setCompDivisionId(int compDivisionId) {
        this.compDivisionId = compDivisionId;
    }

    public int getParentDivisionSort() {
        return parentDivisionSort;
    }

    public void setParentDivisionSort(int parentDivisionSort) {
        this.parentDivisionSort = parentDivisionSort;
    }
}
