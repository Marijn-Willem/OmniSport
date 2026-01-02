package com.sports.cache.data;

import com.sports.cache.key.CacheDataKey;
import com.sports.cache.key.DartsMatchKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.PersonMatch;
import com.sports.entity.PersonMatchPart;
import com.sports.entity.Sport;
import com.sports.entity.comparator.PersonMatchPartId;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.PersonMatchManager;
import com.sports.entity.manager.PersonMatchPartManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DartsMatchData extends OutputData {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;
    private final Integer clientId;
    private final int nestingLevelNested = YamlUtil.getLevelForNestedFragment(nestingLevel);

    private boolean isValidOutput;
    private PersonSportFragment personSport1;
    private PersonSportFragment personSport2;
    private boolean finished;
    private boolean person1Win;
    private boolean person1Start;
    private DartsMatchStatsFragment matchStats;
    private final List<DartsSetStatsFragment> setStats = new ArrayList<>();

    public DartsMatchData(int competitionId, int seasonId, int personMatchId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
        this.clientId = clientId;
    }

    @Override
    public CacheDataKey getCacheDataKey() {
        return new DartsMatchKey(competitionId, seasonId, personMatchId, clientId);
    }

    @Override
    public void fill(Statement stat) throws SQLException {
        PersonMatchKey personMatchKey = new PersonMatchKey(
                new CompSeasonKey(competitionId, seasonId),
                personMatchId
        );

        int sportId = new CompetitionManager(stat).getCompetition(personMatchKey.getCompetitionId()).getSportId();
        if (sportId == Sport.sportIdDarts) {
            PersonMatch personMatch = new PersonMatchManager(stat).getPersonMatch(personMatchKey);
            if (personMatch != null) {
                isValidOutput = true;
                personSport1 = getFilledPersonSportFragment(personMatchKey, personMatch.getPersonSport1Id(), stat);
                personSport2 = getFilledPersonSportFragment(personMatchKey, personMatch.getPersonSport2Id(), stat);
                finished = personMatch.isFinished();
                person1Win = personMatch.isParticipant1Win();
                person1Start = personMatch.isPerson1Start();
                matchStats = DataFragmentUtil.getFilledDataFragment(
                        new DartsMatchStatsFragment(competitionId, seasonId, personMatchId,
                                YamlUtil.getLevelForNestedFragment(nestingLevelNested)), getCacheDataKey(), stat);

                List<PersonMatchPart> sets = new PersonMatchPartManager(stat).getPersonMatchPartsWithoutParent(personMatchKey);
                sets.sort(new PersonMatchPartId());

                sets.forEach(x -> setStats.add(
                                new DartsSetStatsFragment(competitionId, seasonId, personMatchId, x.getPersonMatchPartId(),
                                        YamlUtil.getLevelForNestedList(nestingLevelNested))
                        )
                );

                DataFragmentUtil.fillDataFragments(setStats, getCacheDataKey());
            }
        }
    }

    @Override
    public boolean isValidOutput() {
        return isValidOutput;
    }

    @Override
    public String toXML() {
        return XmlUtil.getOpeningTag("dartsMatch") +
                XmlUtil.getNullableFragmentAsTag("personSport1", personSport1) +
                XmlUtil.getNullableFragmentAsTag("personSport2", personSport2) +
                XmlUtil.getTag("finished", finished) +
                XmlUtil.getTag("person1Win", person1Win) +
                XmlUtil.getTag("person1Start", person1Start) +
                XmlUtil.getFragmentAsTag("matchStats", matchStats) +
                XmlUtil.getEnclosedXmlList("setStats", "setStat", setStats) +
                "</dartsMatch>";
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.encloseContent("dartsMatch",
                JsonUtil.getNullableFragmentAsEntry("personSport1", personSport1) + "," +
                        JsonUtil.getNullableFragmentAsEntry("personSport2", personSport2) + "," +
                        JsonUtil.getEntry("finished", finished) + "," +
                        JsonUtil.getEntry("person1Win", person1Win) + "," +
                        JsonUtil.getEntry("person1Start", person1Start) + "," +
                        JsonUtil.getFragmentAsEntry("matchStats", matchStats) + "," +
                        JsonUtil.getArray("setStats", setStats)) + "}";
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtilNested = new YamlUtil(nestingLevelNested);

        return new YamlUtil(nestingLevel).getEntryHeader("dartsMatch") +
                yamlUtilNested.getNullableFragmentAsEntry("personSport1", personSport1) +
                yamlUtilNested.getNullableFragmentAsEntry("personSport2", personSport2) +
                yamlUtilNested.getEntry("finished", finished) +
                yamlUtilNested.getEntry("person1Win", person1Win) +
                yamlUtilNested.getEntry("person1Start", person1Start) +
                yamlUtilNested.getFragmentAsEntry("matchStats", matchStats) +
                yamlUtilNested.getArray("setStats", setStats);
    }

    private PersonSportFragment getFilledPersonSportFragment(PersonMatchKey personMatchKey,
                                                             Integer personSportId,
                                                             Statement stat) throws SQLException {
        if (personSportId != null) {
            PersonSportFragment personSportFragment = new PersonSportFragment(
                    personMatchKey.getCompetitionId(), personMatchKey.getSeasonId(), personSportId, clientId,
                    YamlUtil.getLevelForNestedFragment(nestingLevelNested), false);

            return DataFragmentUtil.getFilledDataFragment(personSportFragment, getCacheDataKey(), stat);
        }

        return null;
    }
}
