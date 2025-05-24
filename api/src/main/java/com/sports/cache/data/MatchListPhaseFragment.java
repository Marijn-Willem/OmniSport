package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.MatchListPhaseKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.H2HMatch;
import com.sports.entity.manager.CompSeasonPhaseManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MatchListPhaseFragment extends WritableFragment {
    final int competitionId;
    final int seasonId;
    final int compSeasonPhaseId;
    final int clientId;

    private CompSeasonPhaseFragment compSeasonPhaseFragment;
    private final List<H2HMatchFragment> h2HMatchFragments = new ArrayList<>();

    public MatchListPhaseFragment(int competitionId, int seasonId, int compSeasonPhaseId,
                                  List<H2HMatch> h2HMatches, int clientId, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonPhaseId = compSeasonPhaseId;
        this.clientId = clientId;

        int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);

        h2HMatchFragments.addAll(h2HMatches.stream().map(x ->
                new H2HMatchFragment(x, clientId, nestingLevelList)).toList());
    }

    @Override
    public CacheKey getCacheKey() {
        return new MatchListPhaseKey(competitionId, seasonId, compSeasonPhaseId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonPhaseKey compSeasonPhaseKey = new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId),
                compSeasonPhaseId);
        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(compSeasonPhaseKey);

        compSeasonPhaseFragment = DataFragmentUtil.getFilledDataFragment(
                new CompSeasonPhaseFragment(compSeasonPhase, clientId, nestingLevel, true),
                getCacheDataKey(), stat);
        DataFragmentUtil.fillDataFragments(h2HMatchFragments, getCacheDataKey());
    }

    @Override
    public String toXML() {
        return compSeasonPhaseFragment.toXML() +
                XmlUtil.getEnclosedXmlList("matchList", "match", h2HMatchFragments);
    }

    @Override
    public String toJson() {
        return compSeasonPhaseFragment.toJson() + "," +
                JsonUtil.getArray("matchList", h2HMatchFragments);
    }

    @Override
    public String toYaml() {
        return compSeasonPhaseFragment.toYaml() +
                new YamlUtil(nestingLevel).getArray("matchList", h2HMatchFragments);
    }
}
