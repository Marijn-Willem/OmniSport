package com.sports.cache.util;

import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CacheKey;
import com.sports.logic.async.ThreadUtil;
import com.sports.logic.async.ThreadWorker;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class CacheUtil {
    private static CacheManager cacheManager;
    private static Cache<String, Serializable> apiCache;

    public static void init() {
        URL url = CacheUtil.class.getResource("/cache-config.xml");
        assert url != null;

        Configuration xmlConfig = new XmlConfiguration(url);
        cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        apiCache = cacheManager.getCache("apicache", String.class, Serializable.class);
    }

    public static void close() {
        cacheManager.close();
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
        apiCache.put(cacheKey.getStringRepresentation(), new CacheObject<>(value));
    }

    private static void deleteFromCache(String stringRepresentation) {
        apiCache.remove(stringRepresentation);
    }

    private static class GetFromCacheExecutor<T> {
        private final String stringRepresentation;
        private T value;

        public GetFromCacheExecutor(String stringRepresentation) {
            this.stringRepresentation = stringRepresentation;
        }

        private void execute() {
            CacheObject<T> cacheObject = (CacheObject<T>) apiCache.get(stringRepresentation);

            if (cacheObject != null) {
                value = cacheObject.dataObject;
                updateTimeLastRetrieved(cacheObject);
            }
        }

        public T getValue() {
            return value;
        }

        private void updateTimeLastRetrieved(CacheObject<T> cacheObject) {
            ThreadWorker tw = () -> {
                cacheObject.timeLastRetrieved = LocalDateTime.now();
                apiCache.put(stringRepresentation, cacheObject);
            };

            ThreadUtil.executeAsync(Collections.singletonList(tw), false, 1);
        }
    }

    private static class CacheListExecutor {
        private final List<CacheListObject> cacheList = new ArrayList<>();

        private void execute() {
            apiCache.forEach(entry -> cacheList.add(
                getCacheListObject(entry.getKey(), (CacheObject<Object>) entry.getValue())
            ));
            cacheList.sort(new CacheListObjectComparator());
        }

        public List<CacheListObject> getCacheList() {
            return cacheList;
        }

        private CacheListObject getCacheListObject(String key, CacheObject<Object> cacheObject) {
            return new CacheListObject(key, cacheObject.timeCreated, cacheObject.timeLastRetrieved);
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
