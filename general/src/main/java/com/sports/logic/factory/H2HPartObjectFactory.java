package com.sports.logic.factory;

import com.sports.entity.H2HMatchPart;
import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartManager;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class H2HPartObjectFactory<MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    private H2HMatchPartManager<MPK, MP> manager;

    private H2HMatchPartManager<MPK, MP> getCachedManager(Statement stat) {
        if (manager == null)
            manager = getMatchPartManager(stat);

        return manager;
    }

    public abstract H2HMatchPartManager<MPK, MP> getMatchPartManager(Statement stat);
    public abstract H2HPartStatObjectFactory<MPSK, MPS> getPartStatObjectFactory();
    public abstract MPK getMatchPartKey(H2HMatchKey h2HMatchKey, int specifId);
    public abstract MP getMatchPart();

    public MP getEntity(Statement stat, H2HMatchKey h2HMatchKey, int specifId) throws SQLException {
        return getCachedManager(stat).getH2HMatchPart(getMatchPartKey(h2HMatchKey, specifId));
    }

    public void insert(Statement stat, H2HMatchKey h2HMatchKey, int specifId, H2HMatchPart matchPart) throws SQLException {
        getCachedManager(stat).insert(getMatchPartKey(h2HMatchKey, specifId), castMatchPart(matchPart));
    }

    private MP castMatchPart(H2HMatchPart matchPart) {
        MP newPart = getMatchPart();
        matchPart.copy(newPart);

        return newPart;
    }
}
