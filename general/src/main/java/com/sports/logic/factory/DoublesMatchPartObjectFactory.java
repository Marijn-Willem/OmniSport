package com.sports.logic.factory;

import com.sports.entity.DoublesMatchPart;
import com.sports.entity.key.DoublesMatchKey;
import com.sports.entity.key.DoublesMatchPartKey;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.manager.DoublesMatchPartManager;
import com.sports.entity.manager.H2HMatchPartManager;

import java.sql.Statement;

public class DoublesMatchPartObjectFactory implements H2HPartObjectFactory<DoublesMatchPartKey, DoublesMatchPart> {
    public H2HMatchPartManager<DoublesMatchPartKey, DoublesMatchPart> getMatchPartManager(Statement stat) {
        return new DoublesMatchPartManager(stat);
    }

    public DoublesMatchPartKey getMatchPartKey(H2HMatchKey h2HMatchKey, int specifId) {
        return new DoublesMatchPartKey((DoublesMatchKey)h2HMatchKey, specifId);
    }

    public DoublesMatchPart getMatchPart() {
        return new DoublesMatchPart();
    }

    public DoublesMatchPartStatObjectFactory getPartStatObjectFactory() {
        return new DoublesMatchPartStatObjectFactory();
    }
}
