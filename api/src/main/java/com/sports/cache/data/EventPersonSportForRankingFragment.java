package com.sports.cache.data;

import com.sports.cache.key.CacheFragmentKey;
import com.sports.cache.key.EventPersonSportForRankingKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.EventPersonSport;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.EventPersonSportKey;
import com.sports.entity.manager.EventPersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;

public class EventPersonSportForRankingFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int personSportId;
    private final int clientId;

    private Integer rank;
    private PersonSportFragment personSportFragment;

    public EventPersonSportForRankingFragment(int competitionId, int seasonId, int compSeasonEventId, int personSportId,
                                              int clientId, int nestingLevel) {
        super(nestingLevel, true);
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.personSportId = personSportId;
        this.clientId = clientId;
    }

    @Override
    void fill(Statement stat) throws SQLException {
        EventPersonSportKey eventPersonSportKey = new EventPersonSportKey(
                new CompSeasonEventKey(new CompSeasonKey(competitionId, seasonId), compSeasonEventId), personSportId);

        EventPersonSport eventPersonSport = new EventPersonSportManager(stat).getEntityFromSuperKey(eventPersonSportKey);

        rank = eventPersonSport.getRank();
        personSportFragment = DataFragmentUtil.getFilledDataFragment(new PersonSportFragment(
                competitionId, seasonId, personSportId, clientId,
                YamlUtil.getLevelForNestedFragment(nestingLevel), false),
                getCacheDataKey(), stat);
    }

    @Override
    public CacheFragmentKey getCacheFragmentKey() {
        return new EventPersonSportForRankingKey(competitionId, seasonId, compSeasonEventId, personSportId);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("rank", rank) +
                XmlUtil.getFragmentAsTag("personSport", personSportFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("rank", rank) + "," +
                JsonUtil.getFragmentAsEntry("personSport", personSportFragment);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("rank", rank, true) +
                yamlUtil.getFragmentAsEntry("personSport", personSportFragment);
    }
}
