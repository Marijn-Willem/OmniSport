package com.sports.cache.util;

import com.sports.cache.data.WritableFragment;
import com.sports.db.type.Point;
import com.sports.entity.Gender;
import com.sports.entity.ParentMatchType;
import com.sports.logic.util.Util;

import java.time.LocalDateTime;
import java.util.List;

public class YamlUtil {
    private static final int nestingLevelOffset = 2;

    private final int nestingLevel;
    private final String prefixStartListItem;
    private final String prefixDefault;
    private final String emptyArrayMarker;

    public YamlUtil(int nestingLevel) {
        this.nestingLevel = nestingLevel;
        prefixStartListItem = Util.padCharacter("- ", ' ', nestingLevel);
        prefixDefault = Util.padCharacter("", ' ', nestingLevel);
        emptyArrayMarker = Util.padCharacter("- ", ' ', getLevelForNestedList(nestingLevel));
    }

    public static int getLevelForNestedFragment(int nestingLevel) {
        return nestingLevel + nestingLevelOffset;
    }

    public static int getLevelForNestedList(int nestingLevel) {
        return nestingLevel + 2 * nestingLevelOffset;
    }

    public String getEntry(String name, String value, boolean isStartListItem) {
        if (value == null)
            return getEmptyEntry(name, isStartListItem);

        String entry = Util.concatStringsWithDelimiter(name, value, ": ");
        return formatEntry(entry, isStartListItem);
    }

    public String getEntry(String name, String value) {
        return getEntry(name, value, false);
    }

    public String getEntry(String name, LocalDateTime value, boolean isStartListItem) {
        if (value == null)
            return getEmptyEntry(name, isStartListItem);

        String entry = Util.concatStringsWithDelimiter(name, Util.convertDateTimeToString(value), ": ");
        return formatEntry(entry, isStartListItem);
    }

    public String getEntry(String name, LocalDateTime value) {
        return getEntry(name, value, false);
    }

    public String getEntry(String name, Integer value, boolean isStartListItem) {
        if (value == null)
            return getEmptyEntry(name, isStartListItem);

        String entry = Util.concatStringsWithDelimiter(name, String.valueOf(value), ": ");
        return formatEntry(entry, isStartListItem);
    }

    public String getEntry(String name, Integer value) {
        return getEntry(name, value, false);
    }

    public String getEntry(String name, boolean value, boolean isStartListItem) {
        String entry = Util.concatStringsWithDelimiter(name, String.valueOf(value), ": ");
        return formatEntry(entry, isStartListItem);
    }

    public String getEntry(String name, boolean value) {
        return getEntry(name, value, false);
    }

    public String getEntry(String name, Point point, boolean isStartListItem) {
        if (point == null)
            return getEmptyEntry(name, isStartListItem);

        return getEntry(name, Util.getGoogleMapsLink(point), isStartListItem);
    }

    public String getEntry(String name, Point point) {
        return getEntry(name, point, false);
    }

    public <T extends WritableFragment> String getArray(String name, List<T> fragments) {
        return getArrayFromRawStrings(name, fragments.stream().map(WritableFragment::toYaml).toList());
    }

    public String getArrayFromCacheList(List<CacheListObject> cacheList) {
        YamlUtil yamlUtilList = new YamlUtil(getLevelForNestedList(nestingLevel));
        return getArrayFromRawStrings("cacheList",
                cacheList.stream().map(x -> getEntryFromCacheListObject(x, yamlUtilList)).toList());
    }

    public String getFragmentAsEntry(String entryName, WritableFragment fragment, boolean isStartListItem) {
        return getEntryHeader(entryName, isStartListItem) + fragment.toYaml();
    }

    public String getFragmentAsEntry(String entryName, WritableFragment fragment) {
        return getFragmentAsEntry(entryName, fragment, false);
    }

    public String getNullableFragmentAsEntry(String entryName, WritableFragment fragment, boolean isStartListItem) {
        return fragment != null ? getFragmentAsEntry(entryName, fragment, isStartListItem) : getEmptyEntry(entryName, isStartListItem);
    }

    public String getNullableFragmentAsEntry(String entryName, WritableFragment fragment) {
        return getNullableFragmentAsEntry(entryName, fragment, false);
    }

    public String getGenderYaml(int genderId) {
        YamlUtil yamlUtilNested = new YamlUtil(getLevelForNestedFragment(nestingLevel));

        return getEntryHeader("gender") +
                yamlUtilNested.getEntry("id", genderId) +
                yamlUtilNested.getEntry("name", Gender.getGenderNameFromId(genderId));
    }

    public String getParentMatchTypeYaml(Integer parentMatchTypeId) {
        if (parentMatchTypeId != null) {
            YamlUtil yamlUtilNested = new YamlUtil(getLevelForNestedFragment(nestingLevel));

            return getEntryHeader("parentMatchType") +
                    yamlUtilNested.getEntry("id", parentMatchTypeId) +
                    yamlUtilNested.getEntry("name", ParentMatchType.getParentMatchTypeNameFromId(parentMatchTypeId));
        }

        return getEmptyEntry("parentMatchType", false);
    }

    public String getEntryHeader(String entryName) {
        return getEntryHeader(entryName, false);
    }

    public String getEntryHeader(String entryName, boolean isStartListItem) {
        return formatEntry(entryName + ":", isStartListItem);
    }

    private String getEmptyEntry(String name, boolean isStartListItem) {
        return formatEntry(name + ": null", isStartListItem);
    }

    private String formatEntry(String line, boolean isStartListItem) {
        String prefix = isStartListItem ? prefixStartListItem : prefixDefault;
        return prefix + line + "\n";
    }

    private String getArrayFromRawStrings(String name, List<String> entries) {
        StringBuilder sb = new StringBuilder(getEntryHeader(name));

        if (entries.isEmpty()) {
            sb.append(emptyArrayMarker);
            sb.append("\n");
        }
        else
            entries.forEach(sb::append);

        return sb.toString();
    }

    private String getEntryFromCacheListObject(CacheListObject cacheListObject, YamlUtil yamlUtilList) {
        return yamlUtilList.getEntry("key", cacheListObject.key(), true) +
                yamlUtilList.getEntry("created", cacheListObject.timeCreated()) +
                yamlUtilList.getEntry("lastRetrieved", cacheListObject.timeLastRetrieved());
    }
}
