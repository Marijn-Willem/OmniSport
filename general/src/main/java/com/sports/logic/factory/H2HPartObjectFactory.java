package com.sports.logic.factory;

import com.sports.entity.H2HMatchPart;
import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartManager;

import java.sql.Statement;

public interface H2HPartObjectFactory<S extends H2HMatchPartKey, T extends H2HMatchPart> {
    H2HMatchPartManager<S, T> getMatchPartManager(Statement stat);
    S getMatchPartKey(H2HMatchKey h2HMatchKey, int specifId);
    T getMatchPart();
    H2HPartStatObjectFactory<? extends H2HMatchPartStatKey, ? extends H2HMatchPartStat>
        getPartStatObjectFactory();
}
