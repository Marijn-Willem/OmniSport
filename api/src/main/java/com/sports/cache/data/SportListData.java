package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.SportListKey;
import com.sports.cache.util.*;
import com.sports.entity.Sport;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.SportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SportListData extends OutputData {
    private final Integer clientId;

    private final List<SportFragment> sportFragments = new ArrayList<>();

    public SportListData(Integer clientId) {
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new SportListKey(clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        List<Sport> sportList = new SportManager(stat).getFullSportList();
        sportList.sort(new NamedEntityName());

        ClientSportFilter filter = new ClientSportFilter(clientId, getCacheDataKey(), stat);
        sportList.stream()
                .map(x -> new SportFragment(x.getId(), clientId, YamlUtil.getLevelForNestedList(nestingLevel), true))
                .filter(filter::isElementAllowed)
                .forEach(sportFragments::add);

        DataFragmentUtil.fillDataFragments(sportFragments, getCacheDataKey());
    }

    @Override
    public boolean isValidOutput() {
        return !sportFragments.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("sportList", "sport", sportFragments);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("sportList", sportFragments) + "}";
    }

    @Override
    public String toYaml() {
        return new YamlUtil(nestingLevel).getArray("sportList", sportFragments);
    }
}
