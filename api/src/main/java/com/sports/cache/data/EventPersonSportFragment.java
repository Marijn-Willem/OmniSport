package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EventPersonSportKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.key.CompSeasonTeamPersonSportKey;
import com.sports.entity.manager.CompSeasonTeamPersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventPersonSportFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int personSportId;
    private final int clientId;

    private PersonSportFragment personSportFragment;
    private CompSeasonTeamFragment compSeasonTeamFragment;

    public EventPersonSportFragment(int competitionId, int seasonId, int compSeasonEventId, int personSportId,
                                    int clientId, int nestingLevel) {
        super(nestingLevel, true);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.personSportId = personSportId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new EventPersonSportKey(competitionId, seasonId, compSeasonEventId, personSportId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        int nestingLevelFragment = DataFragmentUtil.getLevelForNestedFragment(nestingLevel);

        personSportFragment = DataFragmentUtil.getFilledDataFragment(
                new PersonSportFragment(competitionId, seasonId, personSportId, clientId, nestingLevelFragment, false),
                getCacheDataKey(), stat);

        CompSeasonPersonSportKey cspsKey = new CompSeasonPersonSportKey(new CompSeasonKey(competitionId, seasonId),
                personSportId);

        List<CompSeasonTeamPersonSportKey> cstpsKeys = new CompSeasonTeamPersonSportManager(stat)
                .getKeysForCompSeasonPerson(cspsKey);

        if (cstpsKeys.size() == 1) {
            int teamId = cstpsKeys.get(0).getSuperKey().getSpecificId();

            compSeasonTeamFragment = DataFragmentUtil.getFilledDataFragment(
                    new CompSeasonTeamFragment(competitionId, seasonId, teamId, clientId, nestingLevelFragment),
                    getCacheDataKey(), stat);
        }
    }

    @Override
    public String toXML() {
        return XmlUtil.getFragmentAsTag("personSport", personSportFragment) +
                XmlUtil.getNullableFragmentAsTag("team", compSeasonTeamFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getFragmentAsEntry("personSport", personSportFragment) + "," +
                JsonUtil.getNullableFragmentAsEntry("team", compSeasonTeamFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getFragmentAsEntry("personSport", personSportFragment, isInList) +
                yamlUtil.getNullableFragmentAsEntry("team", compSeasonTeamFragment);
    }
}
