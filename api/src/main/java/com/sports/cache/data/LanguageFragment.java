package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.LanguageKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;

import java.sql.SQLException;
import java.sql.Statement;

public class LanguageFragment extends LanguageAsFallbackFragment {
    private LanguageAsFallbackFragment languageAsFallbackFragment;

    public LanguageFragment(int id) {
        super(id);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);
        if (fallBackLanguageId != null)
            languageAsFallbackFragment = DataFragmentUtil.getFilledDataFragment(
                    new LanguageAsFallbackFragment(fallBackLanguageId), getCacheDataKey(), stat);
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
    public CacheKey getCacheKey() {
        return new LanguageKey(id);
    }
}
