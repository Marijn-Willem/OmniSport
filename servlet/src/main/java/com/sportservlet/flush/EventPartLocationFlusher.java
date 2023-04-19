package com.sportservlet.flush;

import com.sports.cache.key.CacheKey;
import com.sports.entity.key.EventPartLocationKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class EventPartLocationFlusher extends CacheFlusher {
    private final EventPartLocationKey eventPartLocationKey;

    public EventPartLocationFlusher(EventPartLocationKey eventPartLocationKey) {
        this.eventPartLocationKey = eventPartLocationKey;
    }

    @Override
    protected List<CacheKey> getCacheKeys(Statement stat) throws SQLException {
        return Collections.singletonList(new com.sports.cache.key.EventPartLocationKey(
                eventPartLocationKey.getSuperKey().getSuperKey().getSuperKey().getCompetitionId(),
                eventPartLocationKey.getSuperKey().getSuperKey().getSuperKey().getSeasonId(),
                eventPartLocationKey.getSuperKey().getSuperKey().getSportEventId(),
                eventPartLocationKey.getSuperKey().getCompSeasonEventPartId(),
                eventPartLocationKey.getEventPartLocationId()
        ));
    }
}
