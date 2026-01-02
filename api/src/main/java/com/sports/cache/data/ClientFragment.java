package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.ClientKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
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
        this(client, 0, false);
    }

    public ClientFragment(Client client, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        id = client.getId();
        name = client.getName();
        passWord = client.getPassWord();
        languageId = client.getLanguageId();
        isAdmin = client.isAdmin();
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new ClientKey(id);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        if (languageId != null)
            languageFragment = DataFragmentUtil.getFilledDataFragment(
                    new LanguageFragment(languageId, YamlUtil.getLevelForNestedFragment(nestingLevel)),
                    getCacheDataKey(), stat);
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

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", id, isInList) +
                yamlUtil.getEntry("name", name) +
                yamlUtil.getEntry("passWord", passWord) +
                yamlUtil.getNullableFragmentAsEntry("language", languageFragment) +
                yamlUtil.getEntry("isAdmin", isAdmin);
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
