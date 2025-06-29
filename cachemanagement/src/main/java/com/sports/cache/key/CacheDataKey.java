package com.sports.cache.key;

import com.sports.cache.util.CacheUtil;

public abstract class CacheDataKey extends CacheKey {
    public CacheKey getMetaKey() {
        return null;
    }
    @Override
    public void delete() {
        CacheKey metaKey = getMetaKey();
        if (metaKey != null)
            metaKey.delete();

        CacheUtil.delete(this);
    }
}
