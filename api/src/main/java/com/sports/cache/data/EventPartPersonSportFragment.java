package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EventPartParticipantKey;
import com.sports.entity.PersonSport;

import java.sql.SQLException;
import java.sql.Statement;

public class EventPartPersonSportFragment extends AlcifoParticipantFragment {
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int personId;

    public EventPartPersonSportFragment(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId,
                                        PersonSport participant, int resultTypeId, Integer resultTypePrecisionId,
                                        int clientId) {
        super(competitionId, seasonId, participant, resultTypeId, resultTypePrecisionId, clientId);
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.personId = participant.getPersonId();
    }

    @Override
    public CacheKey getCacheKey() {
        return new EventPartParticipantKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, participantId);
    }

    @Override
    String getDescription(Statement stat) throws SQLException {
        return getDescriptionPersonSport(personId, stat);
    }
}
