package com.sports.logic.factory;

import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.entity.manager.H2HMatchPartStatManager;

import java.sql.Statement;

public interface H2HPartStatObjectFactory<MPSK extends H2HMatchPartStatKey, MPS extends H2HMatchPartStat> {
    H2HMatchPartStatManager<MPSK, MPS> getStatManager(Statement stat);
    MPSK getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId);
    MPS getMatchPartStat();
}
