package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CompSeasonPhaseKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompSeasonPhase;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CompSeasonPhaseFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonPhaseId;

    private final Integer parentPhaseId;
    private final Integer round;
    private final boolean knockoutParent;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final boolean finished;
    private final boolean hasStanding;
    private final boolean hasDivisionStandings;
    private final Integer bestOf1;
    private final Integer bestOf2;
    private final Integer bestOfDec;
    private final PhaseTypeFragment phaseTypeFragment;
    private final Integer parentMatchTypeId;

    public CompSeasonPhaseFragment(CompSeasonPhase compSeasonPhase, int clientId, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        com.sports.entity.key.CompSeasonPhaseKey compSeasonPhaseKey = compSeasonPhase.getCompSeasonPhaseKey();

        competitionId = compSeasonPhaseKey.getCompetitionId();
        seasonId = compSeasonPhaseKey.getSeasonId();
        compSeasonPhaseId = compSeasonPhaseKey.getCompSeasonPhaseId();

        parentPhaseId = compSeasonPhase.getParentPhaseId();
        round = compSeasonPhase.getRound();
        knockoutParent = compSeasonPhase.isKnockoutParent();
        startDate = compSeasonPhase.getStartDate();
        endDate = compSeasonPhase.getEndDate();
        finished = compSeasonPhase.isFinished();
        hasStanding = compSeasonPhase.isHasStanding();
        hasDivisionStandings = compSeasonPhase.isHasDivisionStandings();
        bestOf1 = compSeasonPhase.getBestOf1();
        bestOf2 = compSeasonPhase.getBestOf2();
        bestOfDec = compSeasonPhase.getBestOfDec();
        phaseTypeFragment = new PhaseTypeFragment(compSeasonPhase.getPhaseTypeId(), clientId,
                YamlUtil.getLevelForNestedFragment(nestingLevel));
        parentMatchTypeId = compSeasonPhase.getParentMatchTypeId();
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new CompSeasonPhaseKey(competitionId, seasonId, compSeasonPhaseId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        DataFragmentUtil.getFilledDataFragment(phaseTypeFragment, getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("compSeasonPhaseId", compSeasonPhaseId) +
                XmlUtil.getTag("parentPhaseId", parentPhaseId) +
                XmlUtil.getTag("round", round) +
                XmlUtil.getTag("knockoutParent", knockoutParent) +
                XmlUtil.getTag("startDate", startDate) +
                XmlUtil.getTag("endDate", endDate) +
                XmlUtil.getTag("finished", finished) +
                XmlUtil.getTag("hasStanding", hasStanding) +
                XmlUtil.getTag("hasDivisionStandings", hasDivisionStandings) +
                XmlUtil.getTag("bestOf1", bestOf1) +
                XmlUtil.getTag("bestOf2", bestOf2) +
                XmlUtil.getTag("bestOfDec", bestOfDec) +
                XmlUtil.getFragmentAsTag("phaseType", phaseTypeFragment) +
                XmlUtil.getParentMatchTypeXML(parentMatchTypeId);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("compSeasonPhaseId", compSeasonPhaseId) + "," +
                JsonUtil.getEntry("parentPhaseId", parentPhaseId) + "," +
                JsonUtil.getEntry("round", round) + "," +
                JsonUtil.getEntry("knockoutParent", knockoutParent) + "," +
                JsonUtil.getEntry("startDate", startDate) + "," +
                JsonUtil.getEntry("endDate", endDate) + "," +
                JsonUtil.getEntry("finished", finished) + "," +
                JsonUtil.getEntry("hasStanding", hasStanding) + "," +
                JsonUtil.getEntry("hasDivisionStandings", hasDivisionStandings) + "," +
                JsonUtil.getEntry("bestOf1", bestOf1) + "," +
                JsonUtil.getEntry("bestOf2", bestOf2) + "," +
                JsonUtil.getEntry("bestOfDec", bestOfDec) + "," +
                JsonUtil.getFragmentAsEntry("phaseType", phaseTypeFragment) + "," +
                JsonUtil.getParentMatchTypeJson(parentMatchTypeId);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("compSeasonPhaseId", compSeasonPhaseId, isInList) +
                yamlUtil.getEntry("parentPhaseId", parentPhaseId) +
                yamlUtil.getEntry("round", round) +
                yamlUtil.getEntry("knockoutParent", knockoutParent) +
                yamlUtil.getEntry("startDate", startDate) +
                yamlUtil.getEntry("endDate", endDate) +
                yamlUtil.getEntry("finished", finished) +
                yamlUtil.getEntry("hasStanding", hasStanding) +
                yamlUtil.getEntry("hasDivisionStandings", hasDivisionStandings) +
                yamlUtil.getEntry("bestOf1", bestOf1) +
                yamlUtil.getEntry("bestOf2", bestOf2) +
                yamlUtil.getEntry("bestOfDec", bestOfDec) +
                yamlUtil.getFragmentAsEntry("phaseType", phaseTypeFragment) +
                yamlUtil.getParentMatchTypeYaml(parentMatchTypeId);
    }
}
