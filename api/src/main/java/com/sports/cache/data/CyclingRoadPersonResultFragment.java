package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CyclingRoadPersonResultKey;
import com.sports.cache.util.*;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.Competition;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.CompetitionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CyclingRoadPersonResultFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int personSportId;
    private final Integer rank;
    private final Integer noCountResultId;
    private final LocalDateTime date;
    private final int clientId;

    private String competitionName;
    private String eventPartName;
    private NoCountResultFragment noCountResultFragment;

    public CyclingRoadPersonResultFragment(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId,
                                           int personSportId, Integer rank, Integer noCountResultId, LocalDateTime date,
                                           int clientId, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.personSportId = personSportId;
        this.rank = rank;
        this.noCountResultId = noCountResultId;
        this.date = date;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new CyclingRoadPersonResultKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, personSportId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(
                new CompSeasonEventKey(compSeasonKey, compSeasonEventId),
                compSeasonEventPartId
        );

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getCompSeasonEventPart(csepKey);

        competitionName = new AliasUtil(clientId, getCacheDataKey(), stat)
                .getAliasableAsClientSpecificString(competition);
        eventPartName = new DescribedEntityUtil(clientId, getCacheDataKey(), stat)
                .getCompSeasonEventPartString(compSeasonEventPart);

        if (noCountResultId != null)
            noCountResultFragment = DataFragmentUtil.getFilledDataFragment(new NoCountResultFragment(noCountResultId,
                            YamlUtil.getLevelForNestedFragment(nestingLevel)), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("competitionId", competitionId) +
                XmlUtil.getTag("competition", competitionName) +
                XmlUtil.getTag("compSeasonEventId", compSeasonEventId) +
                XmlUtil.getTag("compSeasonEventPart", eventPartName) +
                XmlUtil.getTag("date", date) +
                XmlUtil.getTag("rank", rank) +
                XmlUtil.getNullableFragmentAsTag("noCountResult", noCountResultFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("competitionId", competitionId) + "," +
                JsonUtil.getEntry("competition", competitionName) + "," +
                JsonUtil.getEntry("compSeasonEventId", compSeasonEventId) + "," +
                JsonUtil.getEntry("compSeasonEventPart", eventPartName) + "," +
                JsonUtil.getEntry("date", date) + "," +
                JsonUtil.getEntry("rank", rank) + "," +
                JsonUtil.getNullableFragmentAsEntry("noCountResult", noCountResultFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("competitionId", competitionId, isInList) +
                yamlUtil.getEntry("competition", competitionName) +
                yamlUtil.getEntry("compSeasonEventId", compSeasonEventId) +
                yamlUtil.getEntry("compSeasonEventPart", eventPartName) +
                yamlUtil.getEntry("date", date) +
                yamlUtil.getEntry("rank", rank) +
                yamlUtil.getNullableFragmentAsEntry("noCountResult", noCountResultFragment);
    }

    public CompSeasonKey getCompSeasonKey() {
        return new CompSeasonKey(competitionId, seasonId);
    }
}
