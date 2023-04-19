package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.DartsMatchStatsKey;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.darts.DbCalculation;
import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.PersonMatchKey;

import java.sql.SQLException;
import java.sql.Statement;

public class DartsMatchStatsFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;

    private String person1XML;
    private String person2XML;
    private String person1Json;
    private String person2Json;

    public DartsMatchStatsFragment(int competitionId, int seasonId, int personMatchId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new DartsMatchStatsKey(competitionId, seasonId, personMatchId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        PersonMatchKey personMatchKey = new PersonMatchKey(
                new CompSeasonKey(competitionId, seasonId),
                personMatchId
        );

        StatObject statObject = new DbCalculation(stat).getMatchWithStats(personMatchKey);

        person1XML = statObject.getPerson1StatsXmlTags();
        person2XML = statObject.getPerson2StatsXmlTags();
        person1Json = statObject.getPerson1StatsJsonEntries();
        person2Json = statObject.getPerson2StatsJsonEntries();
    }

    @Override
    public String toXML() {
        return XmlUtil.encloseContent("person1Stats", person1XML) +
                XmlUtil.encloseContent("person2Stats", person2XML);
    }

    @Override
    public String toJson() {
        return "\"person1Stats\": {" + person1Json + "}," +
                "\"person2Stats\": {" + person2Json + "}";
    }
}
