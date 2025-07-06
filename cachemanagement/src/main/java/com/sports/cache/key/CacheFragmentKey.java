package com.sports.cache.key;

public abstract class CacheFragmentKey extends CacheKey {
    @Override
    public String getSpecificDeleteUrlPart() {
        return "fragment";
    }
}
