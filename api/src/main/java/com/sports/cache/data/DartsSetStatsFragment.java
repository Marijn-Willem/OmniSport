package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.DartsSetStatsKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.darts.DbCalculation;
import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;

import java.sql.SQLException;
import java.sql.Statement;

public class DartsSetStatsFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;
    private final int personMatchPartId;

    private H2HMatchPartFragment matchPartFragment;
    private String person1XML;
    private String person2XML;
    private String person1Json;
    private String person2Json;

    public DartsSetStatsFragment(int competitionId, int seasonId, int personMatchId, int personMatchPartId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
        this.personMatchPartId = personMatchPartId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new DartsSetStatsKey(competitionId, seasonId, personMatchId, personMatchPartId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        matchPartFragment = DataFragmentUtil.getFilledDataFragment(
                new H2HMatchPartFragment(competitionId, seasonId, personMatchId, personMatchPartId),
                getCacheDataKey(), stat);

        PersonMatchPartKey personMatchPartKey = new PersonMatchPartKey(
                new PersonMatchKey(
                        new CompSeasonKey(competitionId, seasonId),
                        personMatchId
                ),
                personMatchPartId
        );

        StatObject statObject = new DbCalculation(stat).getSetWithStats(personMatchPartKey);

        person1XML = statObject.getPerson1StatsXmlTags();
        person2XML = statObject.getPerson2StatsXmlTags();
        person1Json = statObject.getPerson1StatsJsonEntries();
        person2Json = statObject.getPerson2StatsJsonEntries();
    }

    @Override
    public String toXML() {
        return matchPartFragment.toXML() +
                XmlUtil.encloseContent("person1Stats", person1XML) +
                XmlUtil.encloseContent("person2Stats", person2XML);
    }

    @Override
    public String toJson() {
        return matchPartFragment.toJson() +
                ",\"person1Stats\": {" + person1Json + "}" +
                ",\"person2Stats\": {" + person2Json + "}";
    }
}
