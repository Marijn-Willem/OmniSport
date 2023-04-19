package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.PhaseTypeKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;

import java.sql.SQLException;
import java.sql.Statement;

public class PhaseTypeFragment extends PhaseTypeAsParentFragment {
    private PhaseTypeAsParentFragment parentPhaseType;

    public PhaseTypeFragment(int phaseTypeId, int clientId) {
        super(phaseTypeId, clientId);
    }

    @Override
    public CacheKey getCacheKey() {
        return new PhaseTypeKey(phaseTypeId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);

        if (parentId != null)
            parentPhaseType = DataFragmentUtil.getFilledDataFragment(
                    new PhaseTypeAsParentFragment(parentId, clientId), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getNullableFragmentAsTag("parentPhaseType", parentPhaseType);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getNullableFragmentAsEntry("parentPhaseType", parentPhaseType);
    }
}
