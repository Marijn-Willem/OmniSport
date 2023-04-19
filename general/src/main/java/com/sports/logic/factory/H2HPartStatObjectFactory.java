package com.sports.logic.factory;

import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartStatManager;

import java.sql.Statement;

public interface H2HPartStatObjectFactory<S extends H2HMatchPartStatKey, T extends H2HMatchPartStat> {
    H2HMatchPartStatManager<S, T> getStatManager(Statement stat);
    S getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId);
    T getMatchPartStat();
}
