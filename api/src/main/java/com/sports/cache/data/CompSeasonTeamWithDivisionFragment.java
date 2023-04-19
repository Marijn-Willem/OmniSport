package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.CompSeasonTeam;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonTeamManager;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonTeamWithDivisionFragment extends CompSeasonTeamFragment {
    private CompDivisionFragment compDivisionFragment;

    public CompSeasonTeamWithDivisionFragment(int competitionId, int seasonId, int teamId, int clientId) {
        super(competitionId, seasonId, teamId, clientId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);

        com.sports.entity.key.CompSeasonTeamKey compSeasonTeamKey =
                new com.sports.entity.key.CompSeasonTeamKey(new CompSeasonKey(competitionId, seasonId), teamId);
        CompSeasonTeam compSeasonTeam = new CompSeasonTeamManager(stat).getCompSeasonTeam(compSeasonTeamKey);

        if (compSeasonTeam != null && compSeasonTeam.getCompDivisionId() != null)
            compDivisionFragment = DataFragmentUtil.getFilledDataFragment(
                    new CompDivisionFragment(competitionId, compSeasonTeam.getCompDivisionId()), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getNullableFragmentAsTag("compDivision", compDivisionFragment);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getNullableFragmentAsEntry("compDivision", compDivisionFragment);
    }
}
