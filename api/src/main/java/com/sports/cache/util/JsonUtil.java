package com.sports.cache.util;

import com.sports.cache.data.WritableFragment;
import com.sports.db.type.Point;
import com.sports.entity.Gender;
import com.sports.logic.util.Util;

import java.time.LocalDateTime;
import java.util.List;

public class JsonUtil {
    public static String getEntry(String name, String value) {
        if (value == null)
            return getEmptyEntry(name);

        return getFilledEntry(name, value);
    }

    public static String getEntry(String name, LocalDateTime value) {
        if (value == null)
            return getEmptyEntry(name);

        return getFilledEntry(name, Util.convertDateTimeToString(value));
    }

    public static String getEntry(String name, int value) {
        return "\"" + name + "\": " + value;
    }

    public static String getEntry(String name, Integer value) {
        if (value == null)
            return getEmptyEntry(name);

        return getEntry(name, value.intValue());
    }

    public static String getEntry(String name, boolean value) {
        return "\"" + name + "\": " + value;
    }

    public static String getEntry(String name, Point point) {
        if (point == null)
            return getEmptyEntry(name);

        return getFilledEntry(name, Util.getGoogleMapsLink(point));
    }

    public static String getEmptyEntry(String name) {
        return "\"" + name + "\": null";
    }

    public static String getFragmentAsEntry(String entryName, WritableFragment writableFragment) {
        return "\"" + entryName + "\": {" + writableFragment.toJson() + "}";
    }

    public static String getNullableFragmentAsEntry(String entryName, WritableFragment writableFragment) {
        return writableFragment != null ? getFragmentAsEntry(entryName, writableFragment) : getEmptyEntry(entryName);
    }

    public static <T extends WritableFragment> String getArray(String entryName, List<T> fragments) {
        StringBuilder sb = new StringBuilder("\"" + entryName + "\": [");

        for (int i = 0; i < fragments.size() - 1; i++) {
            sb.append(getFragmentAsEntry(fragments.get(i)));
            sb.append(",");
        }

        if (fragments.size() > 0)
            sb.append(getFragmentAsEntry(fragments.get(fragments.size() - 1)));

        sb.append("]");

        return sb.toString();
    }

    public static String getGenderJson(int genderId) {
        return encloseContent("gender",
                getEntry("id", genderId) + "," +
                        getEntry("name", Gender.getGenderNameFromId(genderId))
        );
    }

    public static String getEmptyResponse() {
        return "{}";
    }

    public static String encloseContent(String name, String content) {
        return "\"" + name + "\": {" + content + "}";
    }

    private static String getFilledEntry(String name, String value) {
        return "\"" + name + "\": \"" + value + "\"";
    }

    private static String getFragmentAsEntry(WritableFragment fragment) {
        return "{" + fragment.toJson() + "}";
    }
}
