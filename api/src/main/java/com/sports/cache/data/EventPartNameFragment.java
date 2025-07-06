package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.EventPartNameKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.EventPartName;
import com.sports.entity.manager.EventPartNameManager;

import java.sql.SQLException;
import java.sql.Statement;

public class EventPartNameFragment extends WritableFragment {
    private final int eventPartNameId;
    private final int clientId;

    private String name;

    public EventPartNameFragment(int eventPartNameId, int clientId, int nestingLevel) {
        super(nestingLevel, false);

        this.eventPartNameId = eventPartNameId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new EventPartNameKey(eventPartNameId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        EventPartName eventPartName = new EventPartNameManager(stat).getEntityFromId(eventPartNameId);

        name = new AliasUtil(clientId, getCacheDataKey(), stat).getAliasableAsClientSpecificString(eventPartName);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("name", name);
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getEntry("name", name);
    }
}
