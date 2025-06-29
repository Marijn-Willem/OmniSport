package com.sports.cache.key;

import com.sports.cache.util.CacheUtil;
import com.sports.logic.util.Util;

import java.io.Serializable;
import java.util.Set;

public abstract class CacheKey implements Serializable {
    abstract String getSpecificKeyPart();
    public String getStringRepresentation() {
        return Util.concatStringsWithDelimiter(getClass().getName().substring(getClass().getPackageName().length() + 1),
                getSpecificKeyPart(), "|");
    }
    public void delete() {
        Set<CacheDataKey> referenceSet = CacheUtil.get(this);
        if (referenceSet != null)
            referenceSet.forEach(CacheDataKey::delete);

        CacheUtil.delete(this);
    }
}
