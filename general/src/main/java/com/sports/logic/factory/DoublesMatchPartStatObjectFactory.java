package com.sports.logic.factory;

import com.sports.entity.DoublesMatchPartStat;
import com.sports.entity.key.DoublesMatchPartKey;
import com.sports.entity.key.DoublesMatchPartStatKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.manager.DoublesMatchPartStatManager;
import com.sports.entity.manager.H2HMatchPartStatManager;

import java.sql.Statement;

public class DoublesMatchPartStatObjectFactory extends H2HPartStatObjectFactory<DoublesMatchPartStatKey, DoublesMatchPartStat> {
    public H2HMatchPartStatManager<DoublesMatchPartStatKey, DoublesMatchPartStat> getStatManager(Statement stat) {
        return new DoublesMatchPartStatManager(stat);
    }

    public DoublesMatchPartStatKey getMatchPartStatKey(H2HMatchPartKey h2HMatchPartKey, int specifId) {
        return new DoublesMatchPartStatKey((DoublesMatchPartKey)h2HMatchPartKey, specifId);
    }

    public DoublesMatchPartStat getMatchPartStat() {
        return new DoublesMatchPartStat();
    }
}
