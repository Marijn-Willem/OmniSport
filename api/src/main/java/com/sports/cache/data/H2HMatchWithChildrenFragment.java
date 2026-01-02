package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.H2HMatch;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class H2HMatchWithChildrenFragment extends H2HMatchFragment {
    private final ArrayList<H2HMatchFragment> childMatchFragments = new ArrayList<>();

    public H2HMatchWithChildrenFragment(H2HMatch h2HMatch, List<H2HMatch> childMatches, int clientId, int nestingLevel) {
        super(h2HMatch, clientId, nestingLevel);

        int nestingLevelList = YamlUtil.getLevelForNestedList(nestingLevel);

        childMatches.forEach(childMatch ->
                childMatchFragments.add(new H2HMatchFragment(childMatch, clientId, nestingLevelList)));
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);
        DataFragmentUtil.fillDataFragments(childMatchFragments, getCacheDataKey());
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getEnclosedXmlList("childMatches", "childMatch", childMatchFragments);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getArray("childMatches", childMatchFragments);
    }

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel).getArray("childMatches", childMatchFragments);
    }
}
