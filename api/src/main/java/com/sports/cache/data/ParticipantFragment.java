package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.ParticipantKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class ParticipantFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int participantId;
    private final int clientId;

    private PersonSportFragment personSportFragment;
    private DoubleFragment doubleFragment;
    private CompSeasonTeamWithDivisionFragment teamFragment;

    public ParticipantFragment(int competitionId, int seasonId, int participantId,
                               int clientId, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.participantId = participantId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new ParticipantKey(competitionId, seasonId, participantId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory =
                new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);

        int nestingLevelNested = DataFragmentUtil.getLevelForNestedFragment(nestingLevel);

        switch (factory.getParticipantType()) {
            case PERSON_SPORT -> personSportFragment = DataFragmentUtil.getFilledDataFragment(
                    new PersonSportFragment(competitionId, seasonId, participantId, clientId, nestingLevelNested, false),
                    getCacheDataKey(), stat);
            case DOUBLE -> doubleFragment = DataFragmentUtil.getFilledDataFragment(
                    new DoubleFragment(competitionId, seasonId, participantId, clientId, nestingLevelNested), getCacheDataKey(), stat);
            case TEAM -> teamFragment = DataFragmentUtil.getFilledDataFragment(
                    new CompSeasonTeamWithDivisionFragment(competitionId, seasonId, participantId, clientId, nestingLevelNested),
                    getCacheDataKey(), stat);
        }
    }

    @Override
    public String toXML() {
        return XmlUtil.getNullableFragmentAsTag("personSport", personSportFragment) +
                XmlUtil.getNullableFragmentAsTag("double", doubleFragment) +
                XmlUtil.getNullableFragmentAsTag("team", teamFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getNullableFragmentAsEntry("personSport", personSportFragment) + "," +
                JsonUtil.getNullableFragmentAsEntry("double", doubleFragment) + "," +
                JsonUtil.getNullableFragmentAsEntry("team", teamFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getNullableFragmentAsEntry("personSport", personSportFragment, isInList) +
                yamlUtil.getNullableFragmentAsEntry("double", doubleFragment) +
                yamlUtil.getNullableFragmentAsEntry("team", teamFragment);
    }
}
