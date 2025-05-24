package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.PersonSportKey;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPersonSportKey;
import com.sports.entity.manager.PersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class PersonSportFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int personSportId;
    private final int clientId;

    private String description;
    private int elo;

    public PersonSportFragment(int competitionId, int seasonId, int personSportId,
                               int clientId, int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);

        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.personSportId = personSportId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new PersonSportKey(new CompSeasonPersonSportKey(new CompSeasonKey(competitionId, seasonId), personSportId));
    }

    @Override
    void fill(Statement stat) throws SQLException {
        List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(
                Collections.singletonList(personSportId));

        if (personSports.size() == 1) {
            PersonSport personSport = personSports.get(0);

            description = new DescribedEntityUtil(clientId, getCacheDataKey(), stat).getPersonSportString(personSport,
                    new CompSeasonKey(competitionId, seasonId));
            elo = personSport.getElo();
        }
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", personSportId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("elo", elo);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", personSportId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("elo", elo);
    }

    @Override
    public String toYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntry("id", personSportId, isInList) +
                yamlUtil.getEntry("description", description) +
                yamlUtil.getEntry("elo", elo);
    }
}
