package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class CompDivision extends SuperKeyEntity implements NamedEntity {
    private String name;
    private Integer parentDivisionId;

    private int compDivisionId;
    private int parentDivisionSort;
    private int nrTeams;
    private Integer firstTeamId;
    private int rowSpanMatchMatrix;

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

    public int getNrTeams() {
        return nrTeams;
    }

    public void increaseNrTeams() {
        nrTeams++;
    }

    public Integer getFirstTeamId() {
        return firstTeamId;
    }

    public void setFirstTeamId(Integer firstTeamId) {
        this.firstTeamId = firstTeamId;
    }

    public int getRowSpanMatchMatrix() {
        return rowSpanMatchMatrix;
    }

    public void increaseRowSpanMatchMatrix(int addedRowSpan) {
        this.rowSpanMatchMatrix += addedRowSpan;
    }

    public void setRowSpanMatchMatrix(int rowSpanMatchMatrix) {
        this.rowSpanMatchMatrix = rowSpanMatchMatrix;
    }
}
