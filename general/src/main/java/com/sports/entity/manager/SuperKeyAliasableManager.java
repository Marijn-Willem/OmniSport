package com.sports.entity.manager;

import com.sports.entity.SuperKeyAliasable;
import com.sports.entity.key.AliasEntityIdKey;
import com.sports.entity.key.SuperKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public abstract class SuperKeyAliasableManager<S extends SuperKey, T extends SuperKeyAliasable> extends SuperKeySuperManager<S, T> {
    abstract T getInstance();

    public SuperKeyAliasableManager(Statement stat) {
        super(stat);
    }

    public void deleteAliasables(Collection<S> superKeys) throws SQLException {
        int aliasEntityId = getInstance().getAliasEntityId();
        List<AliasEntityIdKey> aliasEntityIdKeys = superKeys.stream()
                .map(x -> new AliasEntityIdKey(aliasEntityId, x.getWhiteSpaceSepValues())).toList();

        new AliasManager(stat).delete(aliasEntityIdKeys);

        delete(superKeys);
    }
}
