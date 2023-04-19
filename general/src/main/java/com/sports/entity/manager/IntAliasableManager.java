package com.sports.entity.manager;

import com.sports.entity.IntAliasable;
import com.sports.entity.key.AliasEntityIdKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public abstract class IntAliasableManager<T extends IntAliasable> extends IntSuperManager<T> {
    abstract T getInstance();

    public IntAliasableManager(Statement stat) {
        super(stat);
    }

    @Override
    void delete(int id) throws SQLException {
        AliasEntityIdKey aliasEntityIdKey = new AliasEntityIdKey(getInstance().getAliasEntityId(), Integer.toString(id));
        new AliasManager(stat).delete(Collections.singleton(aliasEntityIdKey));

        super.delete(id);
    }
}
