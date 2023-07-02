package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CyclingRoadStageWinnersKey;
import com.sports.entity.CompSeasonEvent;
import com.sports.entity.SportEvent;
import com.sports.entity.key.ClientCompSeasonKey;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonEventManager;

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
    protected List<CacheKey> getCacheKeys(Statement stat) throws SQLException {
        CompSeasonEventKey cseKey = compSeasonEventPartKey.getSuperKey();
        CompSeasonKey csKey = cseKey.getSuperKey();

        CompSeasonEvent compSeasonEvent = new CompSeasonEventManager(stat).getEntityFromSuperKey(cseKey);

        return new ArrayList<>() {{
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
}
