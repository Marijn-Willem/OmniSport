package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class EventPartName extends SuperKeyAliasable {
    private String name;

    private int eventPartNameId;

    @Override
    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdEventPartName;
    }

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name)
        };
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEventPartNameId() {
        return eventPartNameId;
    }

    public void setEventPartNameId(int eventPartNameId) {
        this.eventPartNameId = eventPartNameId;
    }
}
