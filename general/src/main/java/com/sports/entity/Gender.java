package com.sports.entity;

import java.util.LinkedHashMap;

public class Gender extends IntEntity {
    public static final int genderIdMale = -1;
    public static final int genderIdFemale = -2;
    public static final int genderIdMixed = -3;
    public static final int genderIdOpen = -4;

    public static final LinkedHashMap<Integer, String> genderLinkedHashMap = new LinkedHashMap<>() {{
        put(genderIdMale, "Male");
        put(genderIdFemale, "Female");
        put(genderIdMixed, "Mixed");
        put(genderIdOpen, "Open");
    }};

    @Override
    public int getId() {
        return 0;
    }

    public String[] getPropertiesInSQLStrings() {
        return new String[0];
    }

    public static String getGenderNameFromId(int id) {
        return genderLinkedHashMap.get(id);
    }
}
