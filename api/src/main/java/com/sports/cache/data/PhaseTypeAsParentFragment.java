package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.PhaseTypeAsParentKey;
import com.sports.cache.util.AliasUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.PhaseType;
import com.sports.entity.manager.PhaseTypeManager;

import java.sql.SQLException;
import java.sql.Statement;

public class PhaseTypeAsParentFragment extends WritableFragment {
    final int phaseTypeId;
    final int clientId;

    private String name;
    private boolean isParent;

    Integer parentId;

    public PhaseTypeAsParentFragment(int phaseTypeId, int clientId, int nestingLevel) {
        super(nestingLevel, false);

        this.phaseTypeId = phaseTypeId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new PhaseTypeAsParentKey(phaseTypeId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        PhaseType phaseType = new PhaseTypeManager(stat).getEntityFromId(phaseTypeId);

        name = new AliasUtil(clientId, getCacheDataKey(), stat).getAliasableAsClientSpecificString(phaseType);
        isParent = phaseType.isParent();
        parentId = phaseType.getParentId();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("name", name) +
                XmlUtil.getTag("isParent", isParent);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("name", name) + "," +
                JsonUtil.getEntry("isParent", isParent);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("name", name) +
                yamlUtil.getEntry("isParent", isParent);
    }
}
