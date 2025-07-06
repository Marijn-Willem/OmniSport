package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.CompSeasonParticipantKey;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonParticipantFragment extends WritableFragment {
    final int competitionId;
    final int seasonId;
    final int participantId;
    final int clientId;

    private String description;
    private int elo;

    public CompSeasonParticipantFragment(int competitionId, int seasonId, int participantId,
                                         int clientId, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.participantId = participantId;
        this.clientId = clientId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new CompSeasonParticipantKey(competitionId, seasonId, participantId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonParticipantFactory<? extends com.sports.entity.key.CompSeasonParticipantKey,
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

        Participant participant = factory.getParticipantManager(stat).getEntityFromId(participantId);
        DescribedEntityUtil describedEntityUtil = new DescribedEntityUtil(clientId, getCacheDataKey(), stat);

        elo = participant.getElo();

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        description = describedEntityUtil.getParticipantString(participant, factory.getParticipantType(), compSeasonKey);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", participantId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("elo", elo);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", participantId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("elo", elo);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", participantId, isInList) +
                yamlUtil.getEntry("description", description) +
                yamlUtil.getEntry("elo", elo);
    }
}
