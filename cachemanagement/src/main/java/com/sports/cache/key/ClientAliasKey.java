package com.sports.cache.key;

import com.sports.entity.key.AliasEntityIdKey;
import com.sports.logic.util.Util;

public class ClientAliasKey extends CacheFragmentKey {
    private final int clientId;
    private final AliasEntityIdKey aliasEntityIdKey;

    public ClientAliasKey(int clientId, AliasEntityIdKey aliasEntityIdKey) {
        this.clientId = clientId;
        this.aliasEntityIdKey = aliasEntityIdKey;
    }

    @Override
    public String getSpecificKeyPart() {
        return Util.concatStringsWithDelimiter(Integer.toString(clientId),
                aliasEntityIdKey.getPipeSepValues(), "|");
    }
}
