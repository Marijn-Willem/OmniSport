package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class Alias extends SuperKeyEntity {
    private String entityId;
    private Integer languageId;
    private Integer clientId;
    private String alias;

    private int aliasEntityId;
    private int aliasId;
    private String listDisplayText;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(entityId),
                QueryUtil.convertIntegerToDbValue(languageId),
                QueryUtil.convertIntegerToDbValue(clientId),
                QueryUtil.convertStringToDbValue(alias)
            };
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getAliasEntityId() {
        return aliasEntityId;
    }

    public void setAliasEntityId(int aliasEntityId) {
        this.aliasEntityId = aliasEntityId;
    }

    public int getAliasId() {
        return aliasId;
    }

    public void setAliasId(int aliasId) {
        this.aliasId = aliasId;
    }

    public String getListDisplayText() {
        return listDisplayText;
    }

    public void setListDisplayText(String listDisplayText) {
        this.listDisplayText = listDisplayText;
    }
}
