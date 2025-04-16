package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.util.HashMap;
import java.util.Map;

public class StatType extends IntAliasable {
    public static final int statTypeScoreId = -1;
    public static final int statTypeMisDubId = -2;
    public static final int statTypeThrowId = -3;

    private static final String statTypeScoreName = "Score";
    private static final String statTypeMisDubName = "Missed doubles";
    private static final String statTypeThrowName = "Throw";

    public static final Map<String, Integer> nameIdMap = new HashMap<>() {{
        put(statTypeScoreName, statTypeScoreId);
        put(statTypeMisDubName, statTypeMisDubId);
        put(statTypeThrowName, statTypeThrowId);
    }};

    private String name;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name)
            };
    }

    private int id;

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdStatType;
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
