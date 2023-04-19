package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class NoCountResult extends NamedIntEntity {
    public static final int noCountResultIdDNF = 1;

    private String name;

    private int id;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name)
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

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
