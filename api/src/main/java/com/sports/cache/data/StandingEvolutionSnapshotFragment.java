package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.StandingEvolutionSnapshotKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.Participant;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.calculation.StandingContext;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StandingEvolutionSnapshotFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonPhaseId;
    private final LocalDateTime date;

    private final List<StandingParticipantFragment> standingParticipantFragments = new ArrayList<>();

    public StandingEvolutionSnapshotFragment(int competitionId, int seasonId, int compSeasonPhaseId, int clientId, int nestingLevel,
                                             boolean isDomesticUSA, StandingContext<? extends Participant> standingContext) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonPhaseId = compSeasonPhaseId;

        date = standingContext.date();

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);
        standingParticipantFragments.addAll(standingContext.standing().stream().map(x ->
                new StandingParticipantFragment(compSeasonKey, x, clientId, nestingLevelList, isDomesticUSA)).toList());
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("date", date) +
                XmlUtil.getEnclosedXmlList("participantList", "participant", standingParticipantFragments);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("date", date) + "," +
                JsonUtil.getArray("participantList", standingParticipantFragments);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("date", date, isInList) +
                yamlUtil.getArray("participantList", standingParticipantFragments);
    }

    @Override
    public CacheKey getCacheKey() {
        return new StandingEvolutionSnapshotKey(competitionId, seasonId, compSeasonPhaseId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        for (StandingParticipantFragment fragment : standingParticipantFragments)
            DataFragmentUtil.getFilledDataFragment(fragment, getCacheDataKey(), stat); // Do not fill in parallel,
//         because this fragment is already part of a parallel execution itself,
//         which may lead to an overload in database connections.
    }
}

