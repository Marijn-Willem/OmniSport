package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.ClientAliasKey;
import com.sports.entity.Alias;
import com.sports.entity.key.AliasEntityIdKey;
import com.sports.logic.calculation.DbCalculation;

import java.sql.SQLException;
import java.sql.Statement;

public class ClientAliasFragment extends DataFragment {
    private final int clientId;
    private final AliasEntityIdKey aliasEntityIdKey;

    private Alias alias;

    public ClientAliasFragment(int clientId, AliasEntityIdKey aliasEntityIdKey) {
        this.clientId = clientId;
        this.aliasEntityIdKey = aliasEntityIdKey;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new ClientAliasKey(clientId, aliasEntityIdKey);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        alias = new DbCalculation(stat).getAliasForClient(aliasEntityIdKey, clientId);
    }

    public Alias getAlias() {
        return alias;
    }
}
