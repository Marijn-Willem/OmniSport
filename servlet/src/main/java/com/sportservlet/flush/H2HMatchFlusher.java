package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.StandingParticipantKey;
import com.sports.entity.H2HMatch;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.H2HMatchKey;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class H2HMatchFlusher extends CacheFlusher {
    private final H2HMatchKey h2HMatchKey;

    public H2HMatchFlusher(H2HMatchKey h2HMatchKey) {
        this.h2HMatchKey = h2HMatchKey;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) throws SQLException {
        CompSeasonParticipantFactory factory = new DbCalculation(stat).getCompSeasonParticipantFactory(h2HMatchKey.getCompetitionId());
        H2HMatch h2HMatch = factory.getH2HObjectFactory().getManager(stat).getInstanceFromKey(h2HMatchKey);

        return new ArrayList<>() {{
            StandingParticipantKey sp1Key = getStandingParticipantKey(factory, h2HMatch.getParticipant1Id());
            StandingParticipantKey sp2Key = getStandingParticipantKey(factory, h2HMatch.getParticipant2Id());

            if (sp1Key != null)
                add(sp1Key);

            if (sp2Key != null)
                add(sp2Key);
        }};
    }

    private StandingParticipantKey getStandingParticipantKey(CompSeasonParticipantFactory factory, Integer participantId) {
        if (participantId != null) {
            CompSeasonParticipantKey cspKey = factory.getCompSeasonParticKey(h2HMatchKey.getSuperKey(), participantId);
            return new StandingParticipantKey(cspKey);
        }

        return null;
    }
}
