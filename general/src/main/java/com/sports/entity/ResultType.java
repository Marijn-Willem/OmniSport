package com.sports.entity;

import java.util.LinkedHashMap;

public class ResultType extends IntEntity {
    private static final String resultTypeNamePoints = "Points";
    private static final String resultTypeNameTime = "Time";
    private static final String resultTypeNameDistance = "Distance";

    public static final int resultTypeIdPoints = 1;
    public static final int resultTypeIdTime = 2;
    public static final int resultTypeIdDistance = 3;

    public static final LinkedHashMap<Integer, String> resultTypeLinkedHashMap = new LinkedHashMap<Integer, String>() {{
        put(resultTypeIdPoints, resultTypeNamePoints);
        put(resultTypeIdTime, resultTypeNameTime);
        put(resultTypeIdDistance, resultTypeNameDistance);
    }};

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[0];
    }

    @Override
    public int getId() {
        return 0;
    }
}
