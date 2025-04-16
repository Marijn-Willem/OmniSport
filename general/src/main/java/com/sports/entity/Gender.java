package com.sports.entity;

import java.util.LinkedHashMap;

public class Gender extends IntEntity {
    public static final int genderIdMale = -1;
    public static final int genderIdFemale = -2;
    public static final int genderIdMixed = -3;
    public static final int genderIdOpen = -4;

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

    public static LinkedHashMap<Integer, String> getGenderLinkedHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();

        map.put(genderIdMale, getGenderNameFromId(genderIdMale));
        map.put(genderIdFemale, getGenderNameFromId(genderIdFemale));
        map.put(genderIdMixed, getGenderNameFromId(genderIdMixed));
        map.put(genderIdOpen, getGenderNameFromId(genderIdOpen));

        return map;
    }
}
