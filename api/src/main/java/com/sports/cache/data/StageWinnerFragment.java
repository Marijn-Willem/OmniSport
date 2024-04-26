package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.StageWinnerKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;

public class StageWinnerFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int participantId;
    private final int stage;
    private final int clientId;

    private PersonSportFragment personSportFragment;
    private CompSeasonParticipantFragment compSeasonTeamFragment;

    public StageWinnerFragment(int competitionId, int seasonId, int compSeasonEventId, int participantId,
                               int stage, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.participantId = participantId;
        this.stage = stage;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new StageWinnerKey(competitionId, seasonId, stage);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory =
                new DbCalculation(stat).getCompSeasonParticipantFactory(new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId),
                        compSeasonEventId));

        switch (factory.getParticipantType()) {
            case PERSON_SPORT ->
                personSportFragment = DataFragmentUtil.getFilledDataFragment(
                        new PersonSportFragment(competitionId, seasonId, participantId, clientId), getCacheDataKey(), stat);
            case TEAM ->
                compSeasonTeamFragment = DataFragmentUtil.getFilledDataFragment(
                        new CompSeasonParticipantFragment(competitionId, seasonId, participantId, clientId), getCacheDataKey(), stat);
            case DOUBLE -> {
            }
        }
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("stage", stage) +
                XmlUtil.getNullableFragmentAsTag("personSport", personSportFragment) +
                XmlUtil.getNullableFragmentAsTag("team", compSeasonTeamFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("stage", stage) + "," +
                JsonUtil.getNullableFragmentAsEntry("personSport", personSportFragment) + "," +
                JsonUtil.getNullableFragmentAsEntry("team", compSeasonTeamFragment);
    }
}
