package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.EventPartParticipantKey;
import com.sports.entity.Team;

import java.sql.SQLException;
import java.sql.Statement;

public class EventPartTeamFragment extends AlcifoParticipantFragment {
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final Integer clubId;
    private final Integer nocId;
    private final Integer equipeId;

    public EventPartTeamFragment(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId,
                                 Team participant, int resultTypeId, Integer resultTypePrecisionId, int clientId,
                                 int nestingLevel, boolean isInList) {
        super(competitionId, seasonId, participant, resultTypeId, resultTypePrecisionId, clientId, nestingLevel, isInList);
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.clubId = participant.getClubId();
        this.nocId = participant.getNocId();
        this.equipeId = participant.getEquipeId();
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new EventPartParticipantKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, participantId);
    }

    @Override
    String getDescription(Statement stat) throws SQLException {
        return getDescriptionTeam(clubId, nocId, equipeId, stat);
    }
}
