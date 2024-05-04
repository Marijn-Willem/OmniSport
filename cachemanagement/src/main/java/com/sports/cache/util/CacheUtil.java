package com.sports.cache.util;

import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CacheKey;
import com.sports.logic.util.Util;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class CacheUtil {
    private static final int cacheDuration = 3600;

    private static String groupIdentifier;
    private static MemcachedClient memcachedClient;

    public static void setConfiguration(String groupIdentifier, String host, int port) throws IOException {
        CacheUtil.groupIdentifier = groupIdentifier;
        CacheUtil.memcachedClient = new MemcachedClient(new InetSocketAddress(host, port));
    }

    public static void close() {
        memcachedClient.shutdown();
    }

    public static void setOutputData(OutputData outputData, CacheDataKey cacheDataKey) {
        CacheKey metaCacheKey = outputData.getCacheKey().getMetaKey();

        if (metaCacheKey != null && cacheDataKey != null)
            updateDataFragmentReferences(metaCacheKey.getStringRepresentation(), cacheDataKey);

        memcachedClient.set(getFullKey(outputData.getCacheKey().getStringRepresentation()), cacheDuration, outputData);
    }

    public static void updateDataFragmentReferences(String dataFragmentKey, CacheDataKey cacheDataKey) {
        Set<CacheDataKey> referenceSet = get(dataFragmentKey);
        if (referenceSet == null)
            referenceSet = new HashSet<>();

        referenceSet.add(cacheDataKey);

        memcachedClient.set(getFullKey(dataFragmentKey), cacheDuration, referenceSet);
    }

    public static <T> T get(String cacheKeyAsString) {
        return (T) memcachedClient.get(getFullKey(cacheKeyAsString));
    }

    public static void delete(String cacheKeyAsString) {
        memcachedClient.delete(getFullKey(cacheKeyAsString));
    }

    private static String getFullKey(String cacheKeyAsString) {
        return Util.concatStringsWithDelimiter(groupIdentifier, cacheKeyAsString, "|");
    }
}
