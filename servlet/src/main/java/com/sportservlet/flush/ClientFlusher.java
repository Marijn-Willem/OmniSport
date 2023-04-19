package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.ClientKey;
import com.sports.cache.key.ClientListKey;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientFlusher extends CacheFlusher {
    private final int clientId;

    public ClientFlusher(int clientId) {
        this.clientId = clientId;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) {
        return new ArrayList<>() {{
            add(new ClientListKey());
            add(new ClientKey(clientId));
        }};
    }
}
