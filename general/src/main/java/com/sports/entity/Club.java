package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class Club extends NamedIntEntity {
    private String name;
    private Integer countryGeoId;

    private int id;

    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertIntegerToDbValue(countryGeoId)
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCountryGeoId() {
        return countryGeoId;
    }

    public void setCountryGeoId(Integer countryGeoId) {
        this.countryGeoId = countryGeoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
