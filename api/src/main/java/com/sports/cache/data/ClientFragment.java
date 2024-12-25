package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.ClientKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Client;

import java.sql.SQLException;
import java.sql.Statement;

public class ClientFragment extends WritableFragment {
    private final int id;
    private final String name;
    private final String passWord;
    private final Integer languageId;
    private final boolean isAdmin;

    private LanguageFragment languageFragment;

    public ClientFragment(Client client) {
        id = client.getId();
        name = client.getName();
        passWord = client.getPassWord();
        languageId = client.getLanguageId();
        isAdmin = client.isAdmin();
    }

    @Override
    public CacheKey getCacheKey() {
        return new ClientKey(id);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        if (languageId != null)
            languageFragment = DataFragmentUtil.getFilledDataFragment(
                    new LanguageFragment(languageId), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", id) +
                XmlUtil.getTag("name", name) +
                XmlUtil.getTag("passWord", passWord) +
                XmlUtil.getNullableFragmentAsTag("language", languageFragment) +
                XmlUtil.getTag("isAdmin", isAdmin);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", id) + "," +
                JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("passWord", passWord) + "," +
                JsonUtil.getNullableFragmentAsEntry("language", languageFragment) + "," +
                JsonUtil.getEntry("isAdmin", isAdmin);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassWord() {
        return passWord;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
