package com.sports.cache.util;

import com.sports.cache.data.WritableFragment;
import com.sports.db.type.Point;
import com.sports.entity.Gender;
import com.sports.logic.util.Util;

import java.time.LocalDateTime;
import java.util.List;

public class XmlUtil {
    public static String getTag(String name, Object value) {
        if (value == null)
            return getEmptyTag(name);

        return getFilledTag(name, value.toString());
    }

    public static String getTag(String name, LocalDateTime value) {
        if (value == null)
            return getEmptyTag(name);

        return getFilledTag(name, Util.convertDateTimeToString(value));
    }

    public static String getTag(String name, int value) {
        return getFilledTag(name, Integer.toString(value));
    }

    public static String getTag(String name, boolean value) {
        return getFilledTag(name, Boolean.toString(value));
    }

    public static String getTag(String name, Point point) {
        if (point == null)
            return getEmptyTag(name);

        return getFilledTag(name, Util.getGoogleMapsLink(point));
    }

    public static String getGenderXML(int genderId) {
        return "<gender>" +
                getTag("id", genderId) +
                getTag("name", Gender.getGenderNameFromId(genderId)) +
                "</gender>";
    }

    public static String getEmptyTag(String name) {
        return "<" + name + " xsi:nil=\"true\" />";
    }

    public static String getFragmentAsTag(String tagName, WritableFragment writableFragment) {
        return encloseContent(tagName, writableFragment.toXML());
    }

    public static String getNullableFragmentAsTag(String tagName, WritableFragment writableFragment) {
        return writableFragment != null ? getFragmentAsTag(tagName, writableFragment) : getEmptyTag(tagName);
    }

    public static <T extends WritableFragment> String getTopLevelXmlList(String listTagName,
                                                                         String elementTagName,
                                                                         List<T> fragments) {
        StringBuilder sb = new StringBuilder(getOpeningTag(listTagName));

        for (T fragment : fragments)
            sb.append(encloseContent(elementTagName, fragment.toXML()));

        sb.append(getClosingTag(listTagName));

        return sb.toString();
    }

    public static <T extends WritableFragment> String getEnclosedXmlList(String listTagName,
                                                                         String elementTagName,
                                                                         List<T> fragments) {
        StringBuilder sb = new StringBuilder();

        for (T fragment : fragments)
            sb.append(encloseContent(elementTagName, fragment.toXML()));

        return encloseContent(listTagName, sb.toString());
    }

    public static String getOpeningTag(String name) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><" + name +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
    }

    public static String encloseContent(String name, String content) {
        return "<" + name + ">" + content + getClosingTag(name);
    }

    private static String getFilledTag(String name, String value) {
        return encloseContent(name, escapeXml(value));
    }

    private static String escapeXml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private static String getClosingTag(String name) {
        return "</" + name + ">";
    }
}
