package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.SpeedSkatingHeatKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.EventPartPersonSport;
import com.sports.entity.Sport;
import com.sports.entity.comparator.EventPartPersonPersonId;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.EventPartPersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SpeedSkatingHeatData extends OutputData {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int heat;
    private final Integer clientId;
    private final int nestingLevelNested = DataFragmentUtil.getLevelForNestedFragment(nestingLevel);

    private SpSkHeatPersonSportFragment personSport1Fragment;
    private SpSkHeatPersonSportFragment personSport2Fragment;

    public SpeedSkatingHeatData(int competitionId, int seasonId, int compSeasonEventId, int compSeasonEventPartId, int heat, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.heat = heat;
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheKey() {
        return new SpeedSkatingHeatKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, heat, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        int sportId = new CompetitionManager(stat).getCompetition(competitionId).getSportId();

        if (sportId == Sport.sportIdSpeedSkating) {
            CompSeasonEventPartKey csepKey = new CompSeasonEventPartKey(
                    new CompSeasonEventKey(
                            new CompSeasonKey(competitionId, seasonId), compSeasonEventId
                    ), compSeasonEventPartId
            );

            List<EventPartPersonSport> eventPartPersonSports = new EventPartPersonSportManager(stat)
                    .getEventPartPersonSportList(csepKey, "heat = " + heat);
            eventPartPersonSports.sort(new EventPartPersonPersonId());

            if (eventPartPersonSports.size() == 2) {
                int nestingLevelFragment = DataFragmentUtil.getLevelForNestedFragment(nestingLevelNested);

                personSport1Fragment = DataFragmentUtil.getFilledDataFragment(new SpSkHeatPersonSportFragment(
                        competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, heat,
                        eventPartPersonSports.get(0).getPersonSportId(), clientId, nestingLevelFragment
                ), getCacheKey(), stat);
                personSport2Fragment = DataFragmentUtil.getFilledDataFragment(new SpSkHeatPersonSportFragment(
                        competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, heat,
                        eventPartPersonSports.get(1).getPersonSportId(), clientId, nestingLevelFragment
                ), getCacheKey(), stat);
            }
        }
    }

    @Override
    public boolean isValidOutput() {
        return personSport1Fragment != null && personSport2Fragment != null;
    }

    @Override
    public String toXML() {
        return XmlUtil.encloseContent("heat",
                XmlUtil.getFragmentAsTag("personSport1", personSport1Fragment) +
                        XmlUtil.getFragmentAsTag("personSport2", personSport2Fragment)
        );
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.encloseContent("heat",
                JsonUtil.getFragmentAsEntry("personSport1", personSport1Fragment) + "," +
                JsonUtil.getFragmentAsEntry("personSport2", personSport2Fragment)) + "}";
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);
        YamlUtil yamlUtilNested = new YamlUtil(nestingLevelNested);

        return yamlUtil.getEntryHeader("heat") +
                yamlUtilNested.getFragmentAsEntry("personSport1", personSport1Fragment) +
                yamlUtilNested.getFragmentAsEntry("personSport2", personSport2Fragment);
    }
}
