package com.sports.cache.util;

import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheKey;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.util.HashSet;
import java.util.Set;

public class CacheUtil {
    private static final int cacheDuration = 1800;

    private final static DB db = DBMaker.memoryDB().make();

    private static String groupIdentifier;

    public static void setConfiguration(String groupIdentifier) {
        CacheUtil.groupIdentifier = groupIdentifier;
    }

    public static void close() {
        db.close();
    }

    public static void setOutputData(OutputData outputData, CacheDataKey cacheDataKey) {
        if (cacheDataKey != null) {
            CacheKey metaCacheKey = outputData.getCacheKey().getMetaKey();

            if (metaCacheKey != null)
                updateDataFragmentReferences(metaCacheKey, cacheDataKey);

            putInCache(cacheDataKey, outputData);
        }
    }

    public static void updateDataFragmentReferences(CacheKey referencingKey, CacheDataKey cacheDataKey) {
        Set<CacheDataKey> referenceSet = get(referencingKey);
        if (referenceSet == null)
            referenceSet = new HashSet<>();

        referenceSet.add(cacheDataKey);

        putInCache(referencingKey, referenceSet);
    }

    public static <T> T get(CacheKey cacheKey) {
        GetFromCacheExecutor<T> cacheExecutor = new GetFromCacheExecutor<>(cacheKey);
        cacheExecutor.execute();

        return cacheExecutor.getValue();
    }

    public static void delete(CacheKey cacheKey) {
        new DeleteFromCacheExecutor(cacheKey).execute();
    }

    private static <T> void putInCache(CacheKey cacheKey, T value) {
        new PutInCacheExecutor<>(cacheKey, value).execute();
    }

    private static class PutInCacheExecutor<T> extends CacheExecutor {
        private final CacheKey cacheKey;
        private final T value;

        public PutInCacheExecutor(CacheKey cacheKey, T value) {
            this.cacheKey = cacheKey;
            this.value = value;
        }

        @Override
        void action(HTreeMap<String, Object> cache) {
            cache.put(cacheKey.getStringRepresentation(), value);
        }
    }

    private static class GetFromCacheExecutor<T> extends CacheExecutor {
        private final CacheKey cacheKey;
        private T value;

        public GetFromCacheExecutor(CacheKey cacheKey) {
            this.cacheKey = cacheKey;
        }

        @Override
        void action(HTreeMap<String, Object> cache) {
            value = (T)cache.get(cacheKey.getStringRepresentation());
        }

        public T getValue() {
            return value;
        }
    }

    private static class DeleteFromCacheExecutor extends CacheExecutor {
        private final CacheKey cacheKey;

        public DeleteFromCacheExecutor(CacheKey cacheKey) {
            this.cacheKey = cacheKey;
        }

        @Override
        void action(HTreeMap<String, Object> cache) {
            cache.remove(cacheKey.getStringRepresentation());
        }
    }

    private abstract static class CacheExecutor {
        abstract void action(HTreeMap<String, Object> cache);
        void execute() {
            HTreeMap<String, Object> cache = (HTreeMap<String, Object>)db.hashMap(groupIdentifier)
                    .expireAfterCreate(cacheDuration)
                    .createOrOpen();

            action(cache);

            cache.close();
        }
    }
}
