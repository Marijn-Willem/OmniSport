package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.SportEventKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.SportEvent;
import com.sports.entity.manager.SportEventManager;

import java.sql.SQLException;
import java.sql.Statement;

public class SportEventFragment extends WritableFragment {
    private final int sportId;
    private final int sportEventId;
    private final int clientId;

    private boolean isTeam;
    private boolean isPointsAsc;
    private String name;

    public SportEventFragment(int sportId, int sportEventId, int clientId) {
        this.sportId = sportId;
        this.sportEventId = sportEventId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new SportEventKey(sportId, sportEventId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.SportEventKey sek = new com.sports.entity.key.SportEventKey(sportId, sportEventId);
        SportEvent sportEvent = new SportEventManager(stat).getEntityFromSuperKey(sek);

        isTeam = sportEvent.isTeam();
        isPointsAsc = sportEvent.isPointsSortAsc();
        name = new AliasUtil(clientId, getCacheDataKey(), stat).getAliasableAsClientSpecificString(sportEvent, sek);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("sportEventId", sportEventId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getTag("isTeam", isTeam) +
                XmlUtil.getTag("isPointsAsc", isPointsAsc);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("sportEventId", sportEventId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("isTeam", isTeam) + "," +
                JsonUtil.getEntry("isPointsAsc", isPointsAsc);
    }
}
