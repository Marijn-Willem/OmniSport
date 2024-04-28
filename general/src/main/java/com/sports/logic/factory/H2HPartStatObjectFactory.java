package com.sports.logic.factory;

import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartStatManager;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class H2HPartStatObjectFactory<MPSK extends H2HMatchPartStatKey, MPS extends H2HMatchPartStat> {
    private H2HMatchPartStatManager<MPSK, MPS> manager;

    private H2HMatchPartStatManager<MPSK, MPS> getCachedManager(Statement stat) {
        if (manager == null)
            manager = getStatManager(stat);

        return manager;
    }

    public abstract H2HMatchPartStatManager<MPSK, MPS> getStatManager(Statement stat);
    public abstract MPSK getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId);
    public abstract MPS getMatchPartStat();

    public void insert(Statement stat, H2HMatchPartKey h2HMatchPartKey, int specifId, H2HMatchPartStat h2HMatchPartStat)
        throws SQLException {
        getCachedManager(stat).insert(getMatchPartStatKey(h2HMatchPartKey, specifId), castMatchPartStat(h2HMatchPartStat));
    }

    private MPS castMatchPartStat(H2HMatchPartStat h2HMatchPartStat) {
        MPS newMatchPartStat = getMatchPartStat();
        h2HMatchPartStat.copy(newMatchPartStat);

        return newMatchPartStat;
    }
}
