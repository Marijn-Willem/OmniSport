package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonTeamKey;
import com.sports.entity.key.CompSeasonTeamPersonSportKey;
import com.sports.entity.manager.CompSeasonTeamPersonSportManager;
import com.sports.entity.manager.PersonSportManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompSeasonTeamWithPersonSportsFragment extends CompSeasonParticipantFragment {
    private final List<PersonSportFragment> personSportFragments = new ArrayList<>();

    public CompSeasonTeamWithPersonSportsFragment(int competitionId, int seasonId, int teamId, int clientId, int nestingLevel) {
        super(competitionId, seasonId, teamId, clientId, nestingLevel, true);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        super.fill(stat);

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);
        CompSeasonTeamKey compSeasonTeamKey = new CompSeasonTeamKey(compSeasonKey, participantId);

        List<CompSeasonTeamPersonSportKey> teamPersonSportKeys = new CompSeasonTeamPersonSportManager(stat)
                .getTeamPersonSportsForTeam(compSeasonTeamKey);
        List<Integer> personSportIds = teamPersonSportKeys.stream().map(CompSeasonTeamPersonSportKey::getPersonSportId)
                .toList();

        List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(personSportIds);
        personSports.sort(new DescribedEntityDescription());

        int nestingLevelList = YamlUtil.getLevelForNestedList(nestingLevel);

        personSportFragments.addAll(personSports.stream().map(x ->
                new PersonSportFragment(competitionId, seasonId, x.getId(), clientId, nestingLevelList, true)).toList());

        DataFragmentUtil.fillDataFragments(personSportFragments, getCacheDataKey());
    }

    @Override
    public String toXML() {
        return super.toXML() + XmlUtil.getEnclosedXmlList("personSportList",
                "personSport", personSportFragments);
    }

    @Override
    public String toJson() {
        return super.toJson() + "," + JsonUtil.getArray("personSportList", personSportFragments);
    }

    @Override
    public String toYaml() {
        return super.toYaml() + new YamlUtil(nestingLevel).getArray("personSportList", personSportFragments);
    }
}
