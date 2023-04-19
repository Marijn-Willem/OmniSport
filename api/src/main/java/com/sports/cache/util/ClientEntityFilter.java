package com.sports.cache.util;

import com.sports.cache.data.ClientCompSeasonData;
import com.sports.cache.data.ClientFragment;
import com.sports.cache.data.DataFragment;
import com.sports.cache.key.CacheDataKey;
import com.sports.entity.Client;
import com.sports.entity.manager.ClientManager;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class ClientEntityFilter<T extends DataFragment> {
    private final int clientId;
    final CacheDataKey cacheDataKey;
    private final Statement stat;

    private boolean isPreProcessed;

    private ClientFragment clientFragment;
    ClientCompSeasonData clientCompSeasonData;

    abstract boolean isElementAllowedForNonAdmin(T element);

    abstract void preProcess();

    public ClientEntityFilter(int clientId, CacheDataKey cacheDataKey, Statement stat) throws SQLException {
        this.clientId = clientId;
        this.cacheDataKey = cacheDataKey;
        this.stat = stat;

        init();
    }

    public boolean isElementAllowed(T element) {
        return clientFragment.isAdmin() || processAllowedForNonAdmin(element);
    }

    private void init() throws SQLException {
        Client client = new ClientManager(stat).getEntityFromId(clientId);
        clientFragment = DataFragmentUtil.getFilledDataFragment(new ClientFragment(client), cacheDataKey, stat);

        if (!clientFragment.isAdmin())
            clientCompSeasonData = DataFragmentUtil.getFilledOutputData(new ClientCompSeasonData(clientId), cacheDataKey);
    }

    private boolean processAllowedForNonAdmin(T element) {
        if (!isPreProcessed) {
            preProcess();
            isPreProcessed = true;
        }

        return isElementAllowedForNonAdmin(element);
    }
}
