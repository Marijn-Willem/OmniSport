package com.sports.calc.alcifo;

import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.Entity;
import com.sports.entity.EventPersonSport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.EventPersonSportKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.EventPersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventPersonSportRankFromFinalEventPartUpdater<T extends Entity> {
    private final CompSeasonEventKey compSeasonEventKey;
    private final Statement stat;

    public abstract Map<EventPersonSportKey, T> getSourceMap(CompSeasonEventPartKey csepKey, Statement stat) throws SQLException;
    public abstract Integer getRank(T entity);

    public EventPersonSportRankFromFinalEventPartUpdater(CompSeasonEventKey compSeasonEventKey, Statement stat) {
        this.compSeasonEventKey = compSeasonEventKey;
        this.stat = stat;
    }

    public void updateEventPersonSportRanks() throws SQLException {
        List<CompSeasonEventPart> finalEventParts = new CompSeasonEventPartManager(stat)
                .getCompSeasonEventPartsFromEvents(Collections.singletonList(compSeasonEventKey))
                .stream().filter(CompSeasonEventPart::isFinal).toList();

        if (finalEventParts.size() == 1) {
            CompSeasonEventPart finalEventPart = finalEventParts.get(0);

            EventPersonSportManager epsm = new EventPersonSportManager(stat);
            Map<EventPersonSportKey, EventPersonSport> existingEventPersonSports = epsm.getParticipantMapInEvent(compSeasonEventKey);

            Map<EventPersonSportKey, EventPersonSport> eventPersonsToUpdate = new HashMap<>();

            getSourceMap(finalEventPart.getCompSeasonEventPartKey(), stat).forEach((k, v) -> {
                if (existingEventPersonSports.containsKey(k)) {
                    EventPersonSport existingEventPersonSport = existingEventPersonSports.get(k);
                    Integer rank = getRank(v);

                    if (rank != null) {
                        existingEventPersonSport.setRank(rank);
                        eventPersonsToUpdate.put(k, existingEventPersonSport);
                    }
                }
            });

            epsm.updateParticipantMap(eventPersonsToUpdate);
        }
    }
}
