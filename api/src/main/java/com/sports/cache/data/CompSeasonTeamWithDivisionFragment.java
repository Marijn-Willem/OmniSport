package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.CompSeasonTeam;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonTeamManager;

import java.sql.SQLException;
import java.sql.Statement;

public class CompSeasonTeamWithDivisionFragment extends CompSeasonParticipantFragment {
    private CompDivisionFragment compDivisionFragment;

    public CompSeasonTeamWithDivisionFragment(int competitionId, int seasonId, int teamId,
                                              int clientId, int nestingLevel) {
        super(competitionId, seasonId, teamId, clientId, nestingLevel, false);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);

        com.sports.entity.key.CompSeasonTeamKey compSeasonTeamKey =
                new com.sports.entity.key.CompSeasonTeamKey(new CompSeasonKey(competitionId, seasonId), participantId);
        CompSeasonTeam compSeasonTeam = new CompSeasonTeamManager(stat).getCompSeasonTeam(compSeasonTeamKey);

        if (compSeasonTeam != null && compSeasonTeam.getCompDivisionId() != null)
            compDivisionFragment = DataFragmentUtil.getFilledDataFragment(
                    new CompDivisionFragment(competitionId, compSeasonTeam.getCompDivisionId(),
                            DataFragmentUtil.getLevelForNestedFragment(nestingLevel), false), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getNullableFragmentAsTag("compDivision", compDivisionFragment);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getNullableFragmentAsEntry("compDivision", compDivisionFragment);
    }

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel).getNullableFragmentAsEntry("compDivision", compDivisionFragment);
    }
}
