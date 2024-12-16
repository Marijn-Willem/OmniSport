package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.CompSeasonParticipantListKey;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonParticipantListData extends ParticipantListData {
    public CompSeasonParticipantListData(int competitionId, int seasonId, Integer clientId) {
        super(competitionId, seasonId, clientId);
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new CompSeasonParticipantListKey(competitionId, seasonId, clientId);
    }

    @Override
    List<Integer> getParticipantIds(Statement stat, CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey,
            ? extends Participant,
            ? extends SuperKeyEntity,
            ? extends H2HMatchKey,
            ? extends H2HMatch,
            ? extends H2HMatchPartKey,
            ? extends H2HMatchPart,
            ? extends H2HMatchPartStatKey,
            ? extends H2HMatchPartStat> factory) throws SQLException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        return factory.getCompSeasonParticipantManager(stat).getParticipantIdsCompSeason(compSeasonKey);
    }
}
