package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class ResultTypePrecision extends SuperKeyEntity implements NamedEntity {
    public static final int resultTypePrecisionIdTimeSeconds = 1;
    public static final int resultTypePrecisionIdTimeCentiseconds = 2;
    public static final int resultTypePrecisionIdTimeMilliseconds = 3;

    private String name;

    private int resultTypePrecisionId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name)
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResultTypePrecisionId() {
        return resultTypePrecisionId;
    }

    public void setResultTypePrecisionId(int resultTypePrecisionId) {
        this.resultTypePrecisionId = resultTypePrecisionId;
    }
}
