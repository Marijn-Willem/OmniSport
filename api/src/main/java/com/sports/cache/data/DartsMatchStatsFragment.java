package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.DartsMatchStatsKey;
import com.sports.calc.darts.DbCalculation;
import com.sports.calc.darts.stat.StatObject;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.PersonMatchKey;

import java.sql.SQLException;
import java.sql.Statement;

public class DartsMatchStatsFragment extends DartsStatsFragment {
    private final int competitionId;
    private final int seasonId;
    private final int personMatchId;

    public DartsMatchStatsFragment(int competitionId, int seasonId, int personMatchId, int nestingLevel) {
        super(nestingLevel, false);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personMatchId = personMatchId;
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new DartsMatchStatsKey(competitionId, seasonId, personMatchId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        PersonMatchKey personMatchKey = new PersonMatchKey(
                new CompSeasonKey(competitionId, seasonId),
                personMatchId
        );

        StatObject statObject = new DbCalculation(stat).getMatchWithStats(personMatchKey);

        assert statObject != null;

        fillStatOutputFromStatObject(statObject);
    }

    @Override
    public String toXML() {
        return getStatOutputXML();
    }

    @Override
    public String toJson() {
        return getStatOutputJson();
    }

    @Override
    public String toYaml() {
        return getStatOutputYaml();
    }
}
