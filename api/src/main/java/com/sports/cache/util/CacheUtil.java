package com.sports.cache.util;

import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CacheKey;
import com.sports.logic.async.ThreadUtil;
import com.sports.logic.async.ThreadWorker;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
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

    public static List<CacheListObject> getCacheList() {
        CacheListExecutor executor = new CacheListExecutor();
        executor.execute();

        return executor.getCacheList();
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

    private static class PutInCacheExecutor<T> extends CacheExecutor<T> {
        private final CacheKey cacheKey;
        private final T value;

        public PutInCacheExecutor(CacheKey cacheKey, T value) {
            this.cacheKey = cacheKey;
            this.value = value;
        }

        @Override
        void action(HTreeMap<String, CacheObject<T>> cache) {
            cache.put(cacheKey.getStringRepresentation(), new CacheObject<>(value));
        }
    }

    private static class GetFromCacheExecutor<T> extends CacheExecutor<T> {
        private final String stringRepresentation;
        private T value;

        public GetFromCacheExecutor(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        @Override
        void action(HTreeMap<String, CacheObject<T>> cache) {
            CacheObject<T> cacheObject = cache.get(stringRepresentation);

            if (cacheObject != null) {
                value = cacheObject.dataObject;
                updateTimeLastRetrieved(cache, cacheObject);
            }
        }

        public T getValue() {
            return value;
        }

        private void updateTimeLastRetrieved(HTreeMap<String, CacheObject<T>> cache, CacheObject<T> cacheObject) {
            ThreadWorker tw = () -> {
                cacheObject.timeLastRetrieved = LocalDateTime.now();
                cache.put(stringRepresentation, cacheObject);
            };

            ThreadUtil.executeAsync(Collections.singletonList(tw), false, 1);
        }
    }

    private static class DeleteFromCacheExecutor extends CacheExecutor<Object> {
        private final String stringRepresentation;

        public DeleteFromCacheExecutor(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        @Override
        void action(HTreeMap<String, CacheObject<Object>> cache) {
            cache.remove(stringRepresentation);
        }
    }

    private static class CacheListExecutor extends CacheExecutor<Object> {
        private final List<CacheListObject> cacheList = new ArrayList<>();

        @Override
        void action(HTreeMap<String, CacheObject<Object>> cache) {
            cache.forEach((key, value) -> cacheList.add(getCacheListObject(key, value)));
            cacheList.sort(new CacheListObjectComparator());
        }

        public List<CacheListObject> getCacheList() {
            return cacheList;
        }

        private CacheListObject getCacheListObject(String key, CacheObject<Object> cacheObject) {
            return new CacheListObject(key, cacheObject.timeCreated, cacheObject.timeLastRetrieved);
        }
    }

    private abstract static class CacheExecutor<T> {
        abstract void action(HTreeMap<String, CacheObject<T>> cache);
        void execute() {
            HTreeMap<String, CacheObject<T>> cache = (HTreeMap<String, CacheObject<T>>)db.hashMap("")
                    .expireAfterCreate(cacheDurationSeconds, TimeUnit.SECONDS)
                    .createOrOpen();

            action(cache);
        }
    }

    private static class CacheObject<T> implements Serializable {
        private final T dataObject;
        private final LocalDateTime timeCreated;
        private LocalDateTime timeLastRetrieved;

        public CacheObject(T dataObject) {
            this.dataObject = dataObject;

            LocalDateTime now = LocalDateTime.now();
            timeCreated = now;
            timeLastRetrieved = now;
        }
    }
}
