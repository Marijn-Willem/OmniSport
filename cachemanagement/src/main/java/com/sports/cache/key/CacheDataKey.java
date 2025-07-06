package com.sports.cache.key;

public abstract class CacheDataKey extends CacheKey {
    @Override
    public String getSpecificDeleteUrlPart() {
        return "data";
    }
}
