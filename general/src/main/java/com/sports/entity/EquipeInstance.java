package com.sports.entity;

public class EquipeInstance extends EntityInstance {
    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[0];
    }

    @Override
    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdEquipeInstance;
    }
}
