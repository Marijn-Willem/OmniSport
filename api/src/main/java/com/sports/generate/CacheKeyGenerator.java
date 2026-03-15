package com.sports.generate;

import java.io.IOException;

public class CacheKeyGenerator {
    static void main() throws IOException {
        CacheKeyGenerateUtil.createCacheKeyFiles(GenerateUtil.getLineGroups("CacheKeyTemplate"),
                "CacheFragmentKey");
    }
}
