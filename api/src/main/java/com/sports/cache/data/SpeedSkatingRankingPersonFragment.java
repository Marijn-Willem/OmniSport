package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.SpeedSkatingRankingPersonKey;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.PersonSport;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;

public class SpeedSkatingRankingPersonFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int personSportId;
    private final int personId;
    private final int rank;
    private final double resultPoints;
    private final int clientId;

    private String description;

    public SpeedSkatingRankingPersonFragment(int competitionId, int seasonId, int compSeasonEventId,
                                             int compSeasonEventPartId, PersonSport personSport, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        personSportId = personSport.getId();
        personId = personSport.getPersonId();
        rank = personSport.getRank();
        resultPoints = personSport.getResultPoints();
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new SpeedSkatingRankingPersonKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId, personSportId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        PersonSport personSport = new PersonSport();
        personSport.setPersonId(personId);

        description = new DescribedEntityUtil(clientId, getCacheDataKey(), stat)
                .getPersonSportString(personSport, compSeasonKey);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", personSportId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("rank", rank) +
                XmlUtil.getTag("points", Util.getDoubleAsStringWith3Digits(resultPoints));
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", personSportId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("rank", rank) + "," +
                JsonUtil.getEntry("points", Util.getDoubleAsStringWith3Digits(resultPoints));
    }
}
