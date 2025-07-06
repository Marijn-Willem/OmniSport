package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.ClientCompSeasonKey;

import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class ClientCompSeasonFlusher extends CacheFlusher {
    private final List<Integer> clientIds;

    public ClientCompSeasonFlusher(List<Integer> clientIds) {
        this.clientIds = clientIds;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) {
        return clientIds.stream().map(ClientCompSeasonKey::new).collect(Collectors.toList());
    }
}
