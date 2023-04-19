package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class Language extends NamedIntEntity {
    private String name;
    private Integer fallbackLanguageId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertIntegerToDbValue(fallbackLanguageId)
            };
    }

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFallbackLanguageId() {
        return fallbackLanguageId;
    }

    public void setFallbackLanguageId(Integer fallbackLanguageId) {
        this.fallbackLanguageId = fallbackLanguageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
