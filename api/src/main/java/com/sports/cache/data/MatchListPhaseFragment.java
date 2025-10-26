package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchListPhaseFragment extends WritableFragment {
    final int competitionId;
    final int seasonId;
    final int compSeasonPhaseId;
    final int clientId;

    private CompSeasonPhaseFragment compSeasonPhaseFragment;
    private final List<H2HMatchWithChildrenFragment> h2HMatchFragments = new ArrayList<>();

    public MatchListPhaseFragment(int competitionId, int seasonId, int compSeasonPhaseId,
                                  List<H2HMatch> h2HMatches, int clientId, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonPhaseId = compSeasonPhaseId;
        this.clientId = clientId;

        int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);

        Map<Integer, List<H2HMatch>> parentMatchMap = getParentMatchMap(h2HMatches);
        h2HMatches.forEach(x -> addH2HMatchFragment(x, parentMatchMap, nestingLevelList));
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
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

    private Map<Integer, List<H2HMatch>> getParentMatchMap(List<H2HMatch> h2HMatches) {
        return new HashMap<>() {{
            h2HMatches.forEach(h2HMatch -> {
               if (h2HMatch.getParentMatchId() != null) {
                   int parentMatchId = h2HMatch.getParentMatchId();
                   if (!this.containsKey(parentMatchId))
                       put(parentMatchId, new ArrayList<>());

                   get(parentMatchId).add(h2HMatch);
               }
            });
        }};
    }

    private void addH2HMatchFragment(H2HMatch h2HMatch, Map<Integer, List<H2HMatch>> parentMatchMap, int nestingLevelList) {
        if (h2HMatch.getParentMatchId() == null) {
            List<H2HMatch> childMatches = new ArrayList<>();

            if (parentMatchMap.containsKey(h2HMatch.getSpecificId()))
                childMatches.addAll(parentMatchMap.get(h2HMatch.getSpecificId()));

            h2HMatchFragments.add(new H2HMatchWithChildrenFragment(h2HMatch, childMatches, clientId, nestingLevelList));
        }
    }
}
