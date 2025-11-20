package com.sports.entity;

import java.util.LinkedHashMap;

public class ParentMatchType extends NamedIntEntity {
    public static final int parentMatchTypeIdAggregate = -1;
    public static final int parentMatchTypeIdSeries7 = -2;

    public static final LinkedHashMap<Integer, String> parentMatchTypeLinkedHashMap = new LinkedHashMap<>() {{
        put(0, "-");
        put(parentMatchTypeIdAggregate, "Aggregate");
        put(parentMatchTypeIdSeries7, "Series 7");
    }};

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[0];
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {}
}
