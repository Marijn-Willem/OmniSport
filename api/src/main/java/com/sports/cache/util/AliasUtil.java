package com.sports.cache.util;

import com.sports.cache.data.ClientAliasFragment;
import com.sports.cache.key.CacheDataKey;
import com.sports.entity.Alias;
import com.sports.entity.Aliasable;
import com.sports.entity.IntAliasable;
import com.sports.entity.key.AliasEntityIdKey;
import com.sports.entity.key.SuperKey;

import java.sql.SQLException;
import java.sql.Statement;

public record AliasUtil(int clientId, CacheDataKey cacheDataKey, Statement stat) {
    public String getAliasableAsClientSpecificString(IntAliasable aliasable) throws SQLException {
        return getAliasableAsClientSpecificString(aliasable, Integer.toString(aliasable.getId()));
    }

    public String getAliasableAsClientSpecificString(Aliasable aliasable, SuperKey superKey)
            throws SQLException {
        return getAliasableAsClientSpecificString(aliasable, superKey.getWhiteSpaceSepValues());
    }

    private String getAliasableAsClientSpecificString(Aliasable aliasable, String entityId) throws SQLException {
        AliasEntityIdKey aliasEntityIdKey = new AliasEntityIdKey(aliasable.getAliasEntityId(), entityId);
        Alias alias = DataFragmentUtil.getFilledDataFragment(new ClientAliasFragment(clientId, aliasEntityIdKey),
                cacheDataKey, stat).getAlias();

        return alias != null ? alias.getAlias() : aliasable.getName();
    }
}
