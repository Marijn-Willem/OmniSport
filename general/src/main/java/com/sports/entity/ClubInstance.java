package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class ClubInstance extends EntityInstance {
    private Integer cityGeoId;

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] { QueryUtil.convertIntegerToDbValue(cityGeoId) };
    }

    @Override
    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdClubInstance;
    }

    public Integer getCityGeoId() {
        return cityGeoId;
    }

    public void setCityGeoId(Integer cityGeoId) {
        this.cityGeoId = cityGeoId;
    }
}
