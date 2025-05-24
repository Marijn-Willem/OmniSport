package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.DartsSetStatsKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.calc.darts.DbCalculation;
import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;

import java.sql.SQLException;
import java.sql.Statement;

public class DartsSetStatsFragment extends DartsStatsFragment {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;
    private final int personMatchPartId;

    private H2HMatchPartFragment matchPartFragment;

    public DartsSetStatsFragment(int competitionId, int seasonId, int personMatchId, int personMatchPartId, int nestingLevel) {
        super(nestingLevel, true);

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
                new H2HMatchPartFragment(competitionId, seasonId, personMatchId, personMatchPartId, nestingLevel), // Same nesting level
                getCacheDataKey(), stat);

        PersonMatchPartKey personMatchPartKey = new PersonMatchPartKey(
                new PersonMatchKey(
                        new CompSeasonKey(competitionId, seasonId),
                        personMatchId
                ),
                personMatchPartId
        );

        StatObject statObject = new DbCalculation(stat).getSetWithStats(personMatchPartKey);

        assert statObject != null;

        fillStatOutputFromStatObject(statObject);
    }

    @Override
    public String toXML() {
        return matchPartFragment.toXML() + getStatOutputXML();
    }

    @Override
    public String toJson() {
        return matchPartFragment.toJson() + getStatOutputJson();
    }

    @Override
    public String toYaml() {
        return matchPartFragment.toYaml() + getStatOutputYaml();
    }
}
