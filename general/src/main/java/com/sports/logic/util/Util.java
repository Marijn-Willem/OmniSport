package com.sports.logic.util;

import com.sports.db.type.Point;
import com.sports.entity.ResultTypePrecision;

import java.nio.file.FileSystems;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Util {
    private static final NumberFormat numberFormat2Digits = NumberFormat.getNumberInstance(Locale.GERMAN);
    private static final NumberFormat numberFormat3Digits = NumberFormat.getNumberInstance(Locale.GERMAN);
    private static final String googleMapsUrl = "https://www.google.com/maps/";

    public static final String fileSeparator = FileSystems.getDefault().getSeparator();

    static {
        numberFormat2Digits.setMinimumFractionDigits(2);
        numberFormat2Digits.setMaximumFractionDigits(2);

        numberFormat3Digits.setMinimumFractionDigits(3);
        numberFormat3Digits.setMaximumFractionDigits(3);
    }

    public static LocalDateTime convertStringToDateTime(String str) {
        int year = Integer.parseInt(str.substring(0, 4));
        int month = Integer.parseInt(str.substring(4, 6));
        int day = Integer.parseInt(str.substring(6, 8));

        int hour = 0, minute = 0, second = 0;

        if (str.length() == 17) {
            hour = Integer.parseInt(str.substring(9, 11));
            minute = Integer.parseInt(str.substring(12, 14));
            second = Integer.parseInt(str.substring(15, 17));
        }

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    public static Integer convertStringToInteger(String str) {
        if (!isNumeric(convertNullStringToEmpty(str)))
            return null;

        return Integer.parseInt(str);
    }

    public static String convertDateTimeToString(LocalDateTime dtTm) {
        return convertDateTimeToDateString(dtTm) +
                " " +
                padCharacter(String.valueOf(dtTm.getHour()), '0', 2) + ":" +
                padCharacter(String.valueOf(dtTm.getMinute()), '0', 2) + ":" +
                padCharacter(String.valueOf(dtTm.getSecond()), '0', 2);
    }

    public static String convertDateTimeToDateString(LocalDateTime dtTm) {
        return dtTm.getYear() +
                padCharacter(String.valueOf(dtTm.getMonthValue()), '0', 2) +
                padCharacter(String.valueOf(dtTm.getDayOfMonth()), '0', 2);
    }

    public static LocalDateTime cloneDateTimeIgnoreTime(LocalDateTime dtTm) {
        return LocalDateTime.of(dtTm.getYear(), dtTm.getMonthValue(), dtTm.getDayOfMonth(), 0, 0);
    }

    public static String convertIntegerToString(Integer integer) {
        return integer != null ? integer.toString() : "";
    }

    public static String convertPointToString(Point point) {
        return point != null ? Util.concatStringsWithDelimiter(
                Double.toString(point.x()), Double.toString(point.y()), "|") : "|";
    }

    public static String concatStringsWithDelimiter(String str1, String str2, String deLim) {
        if (isEmptyString(str1))
            return str2;

        if (isEmptyString(str2))
            return str1;

        return str1 + deLim + str2;
    }

    public static String getTimeStringFromMillis(int millis, int resultTypePrecisionId) {
        int millisPart = millis % 1000;
        String hmsPart = getHMSStringFromMillis(millis);
        String formattedMillisPart = null;

        if (resultTypePrecisionId == ResultTypePrecision.resultTypePrecisionIdTimeCentiseconds)
            formattedMillisPart = padZeroesToString(Integer.toString(millisPart / 10), 2);
        else if (resultTypePrecisionId == ResultTypePrecision.resultTypePrecisionIdTimeMilliseconds)
            formattedMillisPart = padZeroesToString(Integer.toString(millisPart), 3);

        return concatStringsWithDelimiter(hmsPart, formattedMillisPart, ".");
    }

    public static String getTimeStringFromMillis(int millis) {
        int minutes = millis / 60000;
        int seconds = (millis % 60000) / 1000;
        int hundredths = (millis % 1000) / 10;

        return padZeroesToString(String.valueOf(minutes)) + ":" +
                padZeroesToString(String.valueOf(seconds)) + ":" +
                padZeroesToString(String.valueOf(hundredths));
    }

    public static int getMillisFromTimeString(String timeStr) {
        String[] parts = timeStr.split(":");

        return 60000 * Integer.parseInt(parts[0]) +
                1000 * Integer.parseInt(parts[1]) +
                10 * Integer.parseInt(parts[2]);
    }

    public static String getHMSStringFromMillis(int millis) {
        int hours = millis / 3600000;
        int minutes = (millis % 3600000) / 60000;
        int seconds = (millis % 60000) / 1000;

        return padZeroesToString(Integer.toString(hours)) + ":" +
                padZeroesToString(Integer.toString(minutes)) + ":" +
                padZeroesToString(Integer.toString(seconds));
    }

    public static int getMillisFromHMSString(String timeStr) {
        String[] parts = timeStr.split(":");

        return 3600000 * Integer.parseInt(parts[0]) +
                60000 * Integer.parseInt(parts[1]) +
                1000 * Integer.parseInt(parts[2]);
    }

    public static int convertEmptyIntegerToZero(Integer igr) {
        if (igr == null)
            return 0;

        return igr;
    }

    /**
     * Compares two integers. They are considered equal when both are <code>NULL</code>
      * @param igr1 the first integer
     * @param igr2 the second integer
     * @return <code>true</code> if they are equal and <code>false</code> otherwise
     */
    public static boolean compareIntegers(Integer igr1, Integer igr2) {
        return compareNullableObjects(igr1, igr2);
    }

    public static boolean compareNullableObjects(Object o1, Object o2) {
        return o1 == null && o2 == null || o1 != null && o1.equals(o2);
    }
    /**
     * Compares two integers. They are considered unequal when both are <code>NULL</code>
     * @param igr1 the first integer
     * @param igr2 the second integer
     * @return <code>true</code> if they are equal and <code>false</code> otherwise
     */
    public static boolean compareIntegersNotNull(Integer igr1, Integer igr2) {
        return igr1 != null && igr1.equals(igr2);
    }

    public static String convertNullStringToEmpty(String str) {
        return str != null ? str : "";
    }

    public static String concatStrings(Collection<String> strArr, String delim) {
        StringBuilder strConcat = new StringBuilder();

        for (String str : strArr) {
            if (!strConcat.isEmpty())
                strConcat.append(delim);

            strConcat.append(str);
        }

        return strConcat.toString();
    }

    public static String concatStrings(String[] strArr, String delim) {
        return concatStrings(Arrays.asList(strArr), delim);
    }

    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty();
    }

    public static String getPrefixedStringOrEmptyString(String str, String prefix) {
        return !isEmptyString(str) ? prefix + str : "";
    }

    public static String getStringBetweenBracketsOrEmptyString(String str) {
        return getStringBetweenCharactersOrEmptyString(str, "(", ")");
    }

    public static String getStringBetweenCharactersOrEmptyString(String str, String char1, String char2) {
        return !isEmptyString(str) ? char1 + str + char2 : "";
    }

    public static <T> List<T> getElementsLeftNotInRight(Collection<T> left, Collection<T> right) {
        List<T> elems = new ArrayList<>();

        Set<T> set = new HashSet<>(right);

        for (T elem : left)
            if (!set.contains(elem))
                elems.add(elem);

        return elems;
    }

    public static String getCommaSepIntList(Collection<Integer> intList) {
        StringBuilder commaSepList = new StringBuilder();

        for (Integer in : intList)
            commaSepList.append((commaSepList.isEmpty()) ? "" : ", ").append(in);

        return commaSepList.toString();
    }

    public static String getListBetweenBrackets(List<String> strList) {
        return !strList.isEmpty() ? "(" + concatStrings(strList, ", ") + ")" : "";
    }

    public static String convertEmptyInteger(Integer i, String altStr) {
        return i != null ? String.valueOf(i) : altStr;
    }

    public static String convertEmptyDateTimeToString(LocalDateTime dtTm, String altStr) {
        return dtTm != null ? convertDateTimeToString(dtTm) : altStr;
    }

    public static String convertEmptyDateTimeToDateString(LocalDateTime dtTm) {
        return convertEmptyDateTimeToDateString(dtTm, "");
    }

    public static String convertEmptyDateTimeToDateString(LocalDateTime dtTm, String altStr) {
        return dtTm != null ? convertDateTimeToDateString(dtTm) : altStr;
    }

    public static String[] concatenateStringArrays(String[] arr1, String[] arr2) {
        String[] concatenation = new String[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, concatenation, 0, arr1.length);
        System.arraycopy(arr2, 0, concatenation, arr1.length, arr2.length);

        return concatenation;
    }

    public static boolean isNumeric(String str) {
        if (!str.isEmpty()) {
            for (int i = 0; i < str.length(); i++)
                if (!Character.isDigit(str.charAt(i)))
                    return false;

            return true;
        }

        return false;
    }

    public static String convertEmptyString(String str, String altStr) {
        if (isEmptyString(str))
            return altStr;

        return str;
    }

    public static String getDoubleAsStringWith2Digits(double d) {
        return numberFormat2Digits.format(d);
    }

    public static String getDoubleAsStringWith3Digits(double d) {
        return numberFormat3Digits.format(d);
    }

    public static String getGoogleMapsLink(Point point) {
        return googleMapsUrl + "@" + point.x() + "," + point.y() + ",17z";
    }

    public static String padCharacter(String str, char c, int lengthTot) {
        int nrChars = lengthTot - str.length();

        return String.valueOf(c).repeat(Math.max(0, nrChars)) + str;
    }

    private static String padZeroesToString(String str) {
        return padZeroesToString(str, 2);
    }

    private static String padZeroesToString(String str, int lengthTot) {
        return padCharacter(str, '0', lengthTot);
    }
}
