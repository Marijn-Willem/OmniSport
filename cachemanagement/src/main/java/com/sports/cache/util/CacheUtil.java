package com.sports.cache.util;

import com.sports.cache.data.OutputData;
import com.sports.cache.key.CacheDataKey;

public class CacheUtil {
    public static void setConfiguration(String groupIdentifier, String host, int port) {}

    public static void close() {}

    public static void setOutputData(OutputData outputData, CacheDataKey cacheDataKey) {}

    public static void updateDataFragmentReferences(String dataFragmentKey, CacheDataKey cacheDataKey) {}

    public static <T> T get(String cacheKeyAsString) {
        return null;
    }

    public static void delete(String cacheKeyAsString) {}
}
