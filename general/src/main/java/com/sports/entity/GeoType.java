package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class GeoType extends IntEntity implements NamedEntity {
    public static final int geoTypeIdCountry = -1;
    public static final int geoTypeIdCity = -2;

    private String name;

    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
