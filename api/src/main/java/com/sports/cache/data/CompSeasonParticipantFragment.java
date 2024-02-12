package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonParticipantKey;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonKey;
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

    public CompSeasonParticipantFragment(int competitionId, int seasonId, int participantId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.participantId = participantId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonParticipantKey(competitionId, seasonId, participantId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonParticipantFactory<? extends com.sports.entity.key.CompSeasonParticipantKey, ? extends SuperKeyEntity> factory =
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
}
