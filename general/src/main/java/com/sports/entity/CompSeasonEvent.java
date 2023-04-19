package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class CompSeasonEvent extends SuperKeyEntity {
    private String externalSource;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {QueryUtil.convertStringToDbValue(externalSource)};
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }
}
