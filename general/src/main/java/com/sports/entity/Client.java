package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class Client extends IntEntity implements NamedEntity {
    public static final int clientIdProcyclingStats = 7;

    private String name;
    private String passWord;
    private Integer languageId;
    private boolean isAdmin;

    private int id;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertStringToDbValue(passWord),
                QueryUtil.convertIntegerToDbValue(languageId),
                QueryUtil.convertBooleanToDbValue(isAdmin)
            };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
