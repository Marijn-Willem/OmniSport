package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class LocationRole extends IntAliasable {
    private String name;

    private int id;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name)
        };
    }

    @Override
    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdLocationRole;
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
}
