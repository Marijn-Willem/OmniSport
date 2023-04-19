package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.MatchListPhaseKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.H2HMatch;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MatchListPhaseFragment extends WritableFragment {
    final int competitionId;
    final int seasonId;
    final int compSeasonPhaseId;

    private final CompSeasonPhaseFragment compSeasonPhaseFragment;
    private final List<H2HMatchFragment> h2HMatchFragments = new ArrayList<>();

    public MatchListPhaseFragment(CompSeasonPhase compSeasonPhase, List<H2HMatch> h2HMatches, int clientId) {
        this.competitionId = compSeasonPhase.getCompSeasonPhaseKey().getCompetitionId();
        this.seasonId = compSeasonPhase.getCompSeasonPhaseKey().getSeasonId();
        this.compSeasonPhaseId = compSeasonPhase.getCompSeasonPhaseKey().getCompSeasonPhaseId();

        compSeasonPhaseFragment = new CompSeasonPhaseFragment(compSeasonPhase, clientId);
        h2HMatchFragments.addAll(h2HMatches.stream().map(x -> new H2HMatchFragment(x, clientId)).toList());
    }

    @Override
    public CacheKey getCacheKey() {
        return new MatchListPhaseKey(competitionId, seasonId, compSeasonPhaseId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        DataFragmentUtil.getFilledDataFragment(compSeasonPhaseFragment, getCacheDataKey(), stat);
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
}
