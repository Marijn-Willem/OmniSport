package com.sports.entity;

import java.util.LinkedHashMap;

public class Gender extends IntEntity {
    public static final int genderIdMale = -1;
    public static final int genderIdFemale = -2;
    public static final int genderIdMixed = -3;
    public static final int genderIdOpen = -4;

    public static final LinkedHashMap<Integer, String> genderLinkedHashMap = new LinkedHashMap<>() {{
        put(genderIdMale, getGenderNameFromId(genderIdMale));
        put(genderIdFemale, getGenderNameFromId(genderIdFemale));
        put(genderIdMixed, getGenderNameFromId(genderIdMixed));
        put(genderIdOpen, getGenderNameFromId(genderIdOpen));
    }};

    @Override
    public int getId() {
        return 0;
    }

    public String[] getPropertiesInSQLStrings() {
        return new String[0];
    }

    public static String getGenderNameFromId(int id) {
        return switch (id) {
            case genderIdMale -> "Male";
            case genderIdFemale -> "Female";
            case genderIdMixed -> "Mixed";
            case genderIdOpen -> "Open";
            default -> null;
        };
    }
}
