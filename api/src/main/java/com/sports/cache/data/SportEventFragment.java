package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.SportEventKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.SportEvent;

import java.sql.SQLException;
import java.sql.Statement;

public class SportEventFragment extends WritableFragment {
    private final int sportId;
    private final int sportEventId;
    private final int clientId;

    private String name;
    private final int genderId;
    private final boolean isTeam;

    public SportEventFragment(SportEvent sportEvent, int clientId) {
        sportId = sportEvent.getSportId();
        sportEventId = sportEvent.getSportEventId();
        name = sportEvent.getName();
        genderId = sportEvent.getGenderId();
        isTeam = sportEvent.isTeam();
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new SportEventKey(sportId, sportEventId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.SportEventKey seKey = new com.sports.entity.key.SportEventKey(sportId, sportEventId);
        SportEvent sportEvent = new SportEvent();
        sportEvent.setName(name);
        name = new AliasUtil(clientId, getCacheDataKey(), stat).getAliasableAsClientSpecificString(sportEvent, seKey);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("sportEventId", sportEventId) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getGenderXML(genderId) +
                XmlUtil.getTag("isTeam", isTeam);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("sportEventId", sportEventId) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getGenderJson(genderId) + "," +
                JsonUtil.getEntry("isTeam", isTeam);
    }
}
