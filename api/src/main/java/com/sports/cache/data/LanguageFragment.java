package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.LanguageKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;

import java.sql.SQLException;
import java.sql.Statement;

public class LanguageFragment extends LanguageAsFallbackFragment {
    private LanguageAsFallbackFragment languageAsFallbackFragment;

    public LanguageFragment(int id, int nestingLevel) {
        super(id, nestingLevel);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);
        if (fallBackLanguageId != null)
            languageAsFallbackFragment = DataFragmentUtil.getFilledDataFragment(
                    new LanguageAsFallbackFragment(fallBackLanguageId,
                            DataFragmentUtil.getLevelForNestedFragment(nestingLevel)), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getNullableFragmentAsTag("fallbackLanguage", languageAsFallbackFragment);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getNullableFragmentAsEntry("fallbackLanguage", languageAsFallbackFragment);
    }

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel)
                .getNullableFragmentAsEntry("fallbackLanguage", languageAsFallbackFragment);
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new LanguageKey(id);
    }
}
