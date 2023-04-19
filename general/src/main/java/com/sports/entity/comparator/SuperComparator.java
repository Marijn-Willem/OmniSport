package com.sports.entity.comparator;

import java.time.LocalDateTime;
import java.util.Comparator;

public abstract class SuperComparator<T> implements Comparator<T> {
    protected int compareStrings(String s1, String s2) {
        return compareNullableComparables(s1, s2);
    }

    protected int compareIntegers(Integer i1, Integer i2) {
        return compareNullableComparables(i1, i2);
    }

    int compareLocalDateTimes(LocalDateTime ldt1, LocalDateTime ldt2) {
        return compareNullableComparables(ldt1, ldt2);
    }

    private <S> int compareNullableComparables(Comparable<S> c1, S c2) {
        if (c1 == null && c2 == null)
            return 0;

        if (c1 != null && c2 != null)
            return c1.compareTo(c2);

        if (c1 == null)
            return 1;

        return -1;
    }
}
