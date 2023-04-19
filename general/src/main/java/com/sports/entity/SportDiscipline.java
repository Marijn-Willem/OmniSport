package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class SportDiscipline extends SuperKeyAliasable {
    private String name;
    private int resultTypeId;
    private Integer resultTypePrecisionId;

    private int sportDisciplineId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                "" + resultTypeId,
                QueryUtil.convertIntegerToDbValue(resultTypePrecisionId)
            };
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdSportDiscipline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSportDisciplineId() {
        return sportDisciplineId;
    }

    public void setSportDisciplineId(int sportDisciplineId) {
        this.sportDisciplineId = sportDisciplineId;
    }

    public int getResultTypeId() {
        return resultTypeId;
    }

    public void setResultTypeId(int resultTypeId) {
        this.resultTypeId = resultTypeId;
    }

    public Integer getResultTypePrecisionId() {
        return resultTypePrecisionId;
    }

    public void setResultTypePrecisionId(Integer resultTypePrecisionId) {
        this.resultTypePrecisionId = resultTypePrecisionId;
    }
}
