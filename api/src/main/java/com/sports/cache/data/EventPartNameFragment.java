package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EventPartNameKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.EventPartName;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.manager.EventPartNameManager;

import java.sql.SQLException;
import java.sql.Statement;

public class EventPartNameFragment extends WritableFragment {
    private final int sportId;
    private final int sportEventId;
    private final int eventPartNameId;
    private final int clientId;

    private String name;

    public EventPartNameFragment(int sportId, int sportEventId, int eventPartNameId, int clientId) {
        this.sportId = sportId;
        this.sportEventId = sportEventId;
        this.eventPartNameId = eventPartNameId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new EventPartNameKey(sportId, sportEventId, eventPartNameId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.EventPartNameKey eventPartNameKey = new com.sports.entity.key.EventPartNameKey(
                new SportEventKey(sportId, sportEventId), eventPartNameId);

        EventPartName eventPartName = new EventPartNameManager(stat).getEntityFromSuperKey(eventPartNameKey);

        name = new AliasUtil(clientId, getCacheDataKey(), stat)
                .getAliasableAsClientSpecificString(eventPartName, eventPartNameKey);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("name", name);
    }
}
