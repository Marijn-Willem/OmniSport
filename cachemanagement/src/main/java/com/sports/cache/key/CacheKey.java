package com.sports.cache.key;

import com.sports.logic.util.Util;

public abstract class CacheKey {
    abstract String getSpecificKeyPart();
    public abstract String getSpecificDeleteUrlPart();
    public String getStringRepresentation() {
        return Util.concatStringsWithDelimiter(getClass().getName().toLowerCase().substring(
                getClass().getPackageName().length() + 1), getSpecificKeyPart(), "|");
    }
}
