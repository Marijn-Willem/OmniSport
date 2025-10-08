package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CyclingRoadStageWinnersKey;
import com.sports.cache.key.EventPersonSportRankingKey;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.SportEvent;
import com.sports.entity.key.ClientCompSeasonKey;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonEventManager;
import com.sports.entity.manager.CompSeasonEventPartManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CyclingRoadStageFlusher extends CacheFlusher {
    private final CompSeasonEventPartKey compSeasonEventPartKey;

    public CyclingRoadStageFlusher(CompSeasonEventPartKey compSeasonEventPartKey) {
        this.compSeasonEventPartKey = compSeasonEventPartKey;
    }

    @Override
    public List<CacheKey> generateCacheKeys(Statement stat) throws SQLException {
        CompSeasonEventKey cseKey = compSeasonEventPartKey.getSuperKey();
        CompSeasonKey csKey = cseKey.getSuperKey();

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat).getEntityFromSuperKey(compSeasonEventPartKey);
        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);

        return new ArrayList<>() {{
            if (compSeasonEventPart.isFinal())
                addAll(replicateForClientsWithRights(
                        new EventPersonSportRankingReplicator(cseKey.getCompSeasonEventId()), csKey, stat));

            if (compSeasonEvent.getSportEventKey().getSportEventId() == SportEvent.sportEventIdCyclingRoadStage)
                addAll(replicateForClientsWithRights(new StageWinnerReplicator(), csKey, stat));
        }};
    }

    private static class StageWinnerReplicator extends ClientReplicator {
        @Override
        CacheKey getCacheKeyForClientCompSeason(ClientCompSeasonKey clientCompSeasonKey) {
            return new CyclingRoadStageWinnersKey(
                    clientCompSeasonKey.getCompetitionId(),
                    clientCompSeasonKey.getSeasonId(),
                    clientCompSeasonKey.getClientId()
            );
        }
    }

    private static class EventPersonSportRankingReplicator extends ClientReplicator {
        private final int compSeasonEventId;

        public EventPersonSportRankingReplicator(int compSeasonEventId) {
            this.compSeasonEventId = compSeasonEventId;
        }

        @Override
        CacheKey getCacheKeyForClientCompSeason(ClientCompSeasonKey clientCompSeasonKey) {
            return new EventPersonSportRankingKey(
                    clientCompSeasonKey.getCompetitionId(),
                    clientCompSeasonKey.getSeasonId(),
                    compSeasonEventId,
                    clientCompSeasonKey.getClientId()
            );
        }
    }
}
