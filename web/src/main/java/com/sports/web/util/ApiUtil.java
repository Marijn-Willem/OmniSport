package com.sports.web.util;

import com.sports.cache.key.CacheKey;
import com.sports.logic.util.Util;

public class ApiUtil {
    private static String apiUrl;
    private static String apiClient;
    private static String apiPw;

    public static void setApiConfiguration(String apiUrl, String apiClient, String apiPw) {
        ApiUtil.apiUrl = apiUrl;
        ApiUtil.apiClient = apiClient;
        ApiUtil.apiPw = apiPw;
    }

    public static String getCacheDeleteUrl(CacheKey cacheKey) {
        String urlUnencoded = Util.concatStrings(new String[] {
                apiUrl, "delete", cacheKey.getSpecificDeleteUrlPart(),
                cacheKey.getStringRepresentation(), apiClient, apiPw }, "/");

        return Util.encodeURL(urlUnencoded);
    }
}
