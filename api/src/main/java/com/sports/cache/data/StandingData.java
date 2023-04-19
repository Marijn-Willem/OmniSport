package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.Geo;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class StandingData extends OutputData {
    final int competitionId;
    final int seasonId;
    final int compSeasonPhaseId;
    final Integer clientId;

    List<StandingParticipantFragment> standingParticipants = new ArrayList<>();

    public StandingData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonPhaseId = compSeasonPhaseId;
        this.clientId = clientId;
    }

    @Override
    public boolean isValidOutput() {
        return !standingParticipants.isEmpty();
    }

    @Override
    public String toXML() {
        return XmlUtil.getTopLevelXmlList("participantList", "participant", standingParticipants);
    }

    @Override
    public String toJson() {
        return "{" + JsonUtil.getArray("participantList", standingParticipants) + "}";
    }

    CompSeasonPhaseKey getCompSeasonPhaseKey() {
        return new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), compSeasonPhaseId);
    }

    boolean isDomesticUSA(Statement stat) throws SQLException {
        CompetitionFragment fragment = DataFragmentUtil.getFilledDataFragment(new CompetitionFragment(competitionId),
                getCacheKey(), stat);

        return fragment.isDomestic() && fragment.getGeoId() == Geo.geoIdUSA;
    }
}
