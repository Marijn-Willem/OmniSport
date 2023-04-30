package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.SpSkHeatPersonSportKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.alcifo.Calculation;
import com.sports.entity.DisciplinePartPersonSport;
import com.sports.entity.EventDisciplinePart;
import com.sports.entity.Sport;
import com.sports.entity.key.*;
import com.sports.entity.manager.DisciplinePartPersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpSkHeatPersonSportFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int heat;
    private final int personSportId;
    private final int clientId;

    private PersonSportFragment personSportFragment;
    private final List<DisciplinePartFragment> disciplinePartFragments = new ArrayList<>();
    private final List<Integer> cumulativeTimes = new ArrayList<>();
    private final List<Integer> lapTimes = new ArrayList<>();

    public SpSkHeatPersonSportFragment(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId,
                                       int heat, int personSportId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.heat = heat;
        this.personSportId = personSportId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new SpSkHeatPersonSportKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId,
                heat, personSportId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        personSportFragment = DataFragmentUtil.getFilledDataFragment(
                new PersonSportFragment(competitionId, seasonId, personSportId, clientId), getCacheDataKey(), stat);

        List<EventDisciplinePart> eventDisciplineParts = DataFragmentUtil.getFilledDataFragment(
                new EventDisciplinePartListFragment(competitionId, seasonId, Sport.sportIdSpeedSkating, compSeasonEventId, compSeasonEventPartId),
                getCacheDataKey(), stat).getEventDisciplineParts();

        EventPartPersonSportKey eventPartPersonSportKey = new EventPartPersonSportKey(
                new CompSeasonEventPartKey(
                        new CompSeasonEventKey(
                                new CompSeasonKey(competitionId, seasonId), compSeasonEventId
                        ), compSeasonEventPartId
                ), personSportId);

        List<DisciplinePartPersonSport> disciplinePartPersonSports = new DisciplinePartPersonSportManager(stat)
                .getDisciplinePartPersonSportList(Collections.singletonList(eventPartPersonSportKey));

        disciplinePartPersonSports = Calculation.getDisciplinePartPersonsSorted(disciplinePartPersonSports,
                eventDisciplineParts);

        int prevPoints = 0;

        for (int i = 0; i < Math.min(eventDisciplineParts.size(), disciplinePartPersonSports.size()); i++) {
            EventDisciplinePart eventDisciplinePart = eventDisciplineParts.get(i);
            DisciplinePartPersonSport disciplinePartPersonSport = disciplinePartPersonSports.get(i);

            disciplinePartFragments.add(new DisciplinePartFragment(Sport.sportIdSpeedSkating,
                    eventDisciplinePart.getSportDisciplineId(), eventDisciplinePart.getDisciplinePartId(), clientId));

            int points = disciplinePartPersonSport.getPoints();

            cumulativeTimes.add(points);
            lapTimes.add(points - prevPoints);

            prevPoints = points;
        }

        DataFragmentUtil.fillDataFragments(disciplinePartFragments, getCacheDataKey());
    }

    @Override
    public String toXML() {
        StringBuilder sb = new StringBuilder();

        sb.append(personSportFragment.toXML());
        sb.append("<laps>");
        for (int i = 0; i < disciplinePartFragments.size(); i++)
            sb.append(getLapXML(i));
        sb.append("</laps>");

        return sb.toString();
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append(personSportFragment.toJson());
        sb.append(",\"laps\": [");
        for (int i = 0; i < disciplinePartFragments.size() - 1; i++) {
            sb.append(getLapJson(i));
            sb.append(",");
        }
        if (!disciplinePartFragments.isEmpty())
            sb.append(getLapJson(disciplinePartFragments.size() - 1));
        sb.append("]");

        return sb.toString();
    }

    private String getLapXML(int indX) {
        return "<lap>" +
                disciplinePartFragments.get(indX).toXML() +
                XmlUtil.getTag("cumTime", cumulativeTimes.get(indX)) +
                XmlUtil.getTag("lapTime", lapTimes.get(indX)) +
                "</lap>";
    }

    private String getLapJson(int indX) {
        return "{" +
                disciplinePartFragments.get(indX).toJson() + "," +
                JsonUtil.getEntry("cumTime", cumulativeTimes.get(indX)) + "," +
                JsonUtil.getEntry("lapTime", lapTimes.get(indX)) +
                "}";
    }
}
