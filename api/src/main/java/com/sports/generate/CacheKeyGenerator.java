package com.sports.generate;

import java.io.IOException;

public class CacheKeyGenerator {
    public static void main(String[] args) throws IOException {
        CacheKeyGenerateUtil.createCacheKeyFiles(GenerateUtil.getLineGroups("CacheKeyTemplate"),
                "CacheFragmentKey");
    }
}
