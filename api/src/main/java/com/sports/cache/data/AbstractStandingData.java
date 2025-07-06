package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.CompDivision;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.Geo;
import com.sports.entity.key.CompDivisionKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.manager.CompDivisionManager;
import com.sports.entity.manager.CompSeasonPhaseManager;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractStandingData extends OutputData {
    final int competitionId;
    final int seasonId;
    final int compSeasonPhaseId;
    final Integer clientId;
    final int nestingLevelList = DataFragmentUtil.getLevelForNestedList(nestingLevel);

    public AbstractStandingData(int competitionId, int seasonId, int compSeasonPhaseId, Integer clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonPhaseId = compSeasonPhaseId;
        this.clientId = clientId;
    }

    CompSeasonPhaseKey getCompSeasonPhaseKey() {
        return new CompSeasonPhaseKey(new CompSeasonKey(competitionId, seasonId), compSeasonPhaseId);
    }

    boolean isCompSeasonPhaseWithStanding(Statement stat) throws SQLException {
        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(getCompSeasonPhaseKey());

        return compSeasonPhase != null && compSeasonPhase.isHasStanding();
    }

    boolean isCompDivisionWithStanding(int compDivisionId, Statement stat) throws SQLException {
        CompDivisionKey compDivisionKey = new CompDivisionKey(competitionId, compDivisionId);

        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(getCompSeasonPhaseKey());
        CompDivision compDivision = new CompDivisionManager(stat).getCompDivision(compDivisionKey);

        return compSeasonPhase != null && compSeasonPhase.isHasDivisionStandings() && compDivision != null;
    }

    boolean isDomesticUSA(Statement stat) throws SQLException {
        CompetitionFragment fragment = DataFragmentUtil.getFilledDataFragment(new CompetitionFragment(competitionId),
                getCacheDataKey(), stat);

        return fragment.isDomestic() && fragment.getGeoId() == Geo.geoIdUSA;
    }
}
