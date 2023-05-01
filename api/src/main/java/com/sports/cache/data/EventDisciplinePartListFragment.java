package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EventDisciplinePartListKey;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.comparator.EventDisciplinePartOrder;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.SportDisciplineKey;
import com.sports.entity.manager.EventDisciplinePartManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventDisciplinePartListFragment extends DataFragment {
    private final int competitionId;
    private final int seasonId;
    private final int sportId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;

    private final List<EventDisciplinePart> eventDisciplineParts = new ArrayList<>();

    public EventDisciplinePartListFragment(int competitionId, int seasonId, int sportId, int compSeasonEventId, int compSeasonEventPartId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.sportId = sportId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new EventDisciplinePartListKey(competitionId, seasonId, sportId, compSeasonEventId, compSeasonEventPartId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonEventPartKey compSeasonEventPartKey = new CompSeasonEventPartKey(
                new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), compSeasonEventId),
                compSeasonEventPartId
        );

        eventDisciplineParts.addAll(new EventDisciplinePartManager(stat).getEventDisciplineList(compSeasonEventPartKey));

        DbCalculation dbCalc = new DbCalculation(stat);
        SportDisciplineKey sdKey = dbCalc.getSportDisciplineKey(compSeasonEventPartKey);
        dbCalc.fillDisciplinePartsForEventDisciplineParts(sdKey, eventDisciplineParts);
        eventDisciplineParts.sort(new EventDisciplinePartOrder());
    }

    public List<EventDisciplinePart> getEventDisciplineParts() {
        return eventDisciplineParts;
    }
}
