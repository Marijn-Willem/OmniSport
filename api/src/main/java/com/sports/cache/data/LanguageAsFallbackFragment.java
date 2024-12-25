package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.LanguageAsFallbackKey;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Language;
import com.sports.entity.manager.LanguageManager;

import java.sql.SQLException;
import java.sql.Statement;

public class LanguageAsFallbackFragment extends WritableFragment {
    final int id;
    private String name;
    Integer fallBackLanguageId;

    public LanguageAsFallbackFragment(int id) {
        this.id = id;
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", id) + XmlUtil.getTag("name", name);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", id) + "," +
                JsonUtil.getEntry("name", name);
    }

    @Override
    public CacheKey getCacheKey() {
        return new LanguageAsFallbackKey(id);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Language language = new LanguageManager(stat).getEntityFromId(id);

        name = language.getName();
        fallBackLanguageId = language.getFallbackLanguageId();
    }
}
