package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.MatchListCompSeasonKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.H2HMatch;
import com.sports.entity.key.CompSeasonKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchListCompSeasonFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;

    private CompSeasonFragment compSeasonFragment;
    private final List<MatchListPhaseFragment> matchListPhaseFragmentList = new ArrayList<>();

    public MatchListCompSeasonFragment(int competitionId, int seasonId, int clientId,
                                       Map<Integer, List<H2HMatch>> phaseMatchMap, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;

        int nestingLevelList = YamlUtil.getLevelForNestedList(nestingLevel);

        phaseMatchMap.forEach((k, v) -> matchListPhaseFragmentList.add(
                new MatchListPhaseFragment(competitionId, seasonId, k, v, clientId, nestingLevelList))
        );
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new MatchListCompSeasonKey(competitionId, seasonId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        compSeasonFragment = DataFragmentUtil.getFilledDataFragment(new CompSeasonFragment(
                new CompSeasonKey(competitionId, seasonId), nestingLevel, true),
                getCacheDataKey(), stat);

        DataFragmentUtil.fillDataFragments(matchListPhaseFragmentList, getCacheDataKey());
    }

    @Override
    public String toXML() {
        return compSeasonFragment.toXML() +
                XmlUtil.getEnclosedXmlList("compSeasonPhaseList", "compSeasonPhase",
                        matchListPhaseFragmentList);
    }

    @Override
    public String toJson() {
        return compSeasonFragment.toJson() + "," +
                JsonUtil.getArray("compSeasonPhaseList", matchListPhaseFragmentList);
    }

    @Override
    public String toYaml() {
        return compSeasonFragment.toYaml() +
                new YamlUtil(nestingLevel).getArray("compSeasonPhaseList", matchListPhaseFragmentList);
    }

    public CompSeasonKey getCompSeasonKey() {
        return new CompSeasonKey(competitionId, seasonId);
    }
}
