package com.sports.logic.factory;

import com.sports.entity.H2HMatchPart;
import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartManager;

import java.sql.Statement;


public interface H2HPartObjectFactory<MPK extends H2HMatchPartKey,
        MP extends H2HMatchPart,
        MPSK extends H2HMatchPartStatKey,
        MPS extends H2HMatchPartStat> {
    H2HMatchPartManager<MPK, MP> getMatchPartManager(Statement stat);
    MPK getMatchPartKey(H2HMatchKey h2HMatchKey, int specifId);
    MP getMatchPart();
    H2HPartStatObjectFactory<MPSK, MPS> getPartStatObjectFactory();
}
