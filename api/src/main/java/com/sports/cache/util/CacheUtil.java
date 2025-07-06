package com.sports.cache.util;

import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CacheKey;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CacheUtil {
    private static final int cacheDurationSeconds = 1800;

    private static final DB db = DBMaker.memoryDB().make();

    public static void close() {
        db.close();
    }

    public static void setOutputData(OutputData outputData, CacheFragmentKey cacheKey) {
        if (cacheKey != null)
            updateDataFragmentReferences(cacheKey, outputData.getCacheDataKey());

        putInCache(outputData.getCacheDataKey(), outputData);
    }

    public static void updateDataFragmentReferences(CacheFragmentKey referencingKey, CacheDataKey referencedKey) {
        Set<String> referenceSet = get(referencingKey);
        if (referenceSet == null)
            referenceSet = new HashSet<>();

        referenceSet.add(referencedKey.getStringRepresentation());

        putInCache(referencingKey, referenceSet);
    }

    public static <T> T get(CacheKey cacheKey) {
        return get(cacheKey.getStringRepresentation());
    }

    public static void deleteFragment(String stringRepresentation) {
        Set<String> set = get(stringRepresentation);
        if (set != null)
            set.forEach(CacheUtil::deleteData);

        deleteFromCache(stringRepresentation);
    }

    public static void deleteData(String stringRepresentation) {
        deleteFromCache(stringRepresentation);
    }

    private static <T> T get(String stringRepresentation) {
        GetFromCacheExecutor<T> cacheExecutor = new GetFromCacheExecutor<>(stringRepresentation);
        cacheExecutor.execute();

        return cacheExecutor.getValue();
    }

    private static <T> void putInCache(CacheKey cacheKey, T value) {
        new PutInCacheExecutor<>(cacheKey, value).execute();
    }

    private static void deleteFromCache(String stringRepresentation) {
        new DeleteFromCacheExecutor(stringRepresentation).execute();
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
        private final String stringRepresentation;
        private T value;

        public GetFromCacheExecutor(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        @Override
        void action(HTreeMap<String, Object> cache) {
            value = (T)cache.get(stringRepresentation);
        }

        public T getValue() {
            return value;
        }
    }

    private static class DeleteFromCacheExecutor extends CacheExecutor {
        private final String stringRepresentation;

        public DeleteFromCacheExecutor(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        @Override
        void action(HTreeMap<String, Object> cache) {
            cache.remove(stringRepresentation);
        }
    }

    private abstract static class CacheExecutor {
        abstract void action(HTreeMap<String, Object> cache);
        void execute() {
            HTreeMap<String, Object> cache = (HTreeMap<String, Object>)db.hashMap("")
                    .expireAfterCreate(cacheDurationSeconds, TimeUnit.SECONDS)
                    .createOrOpen();

            action(cache);
        }
    }
}
