package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.EventPersonSportKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
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
    private CompSeasonParticipantFragment compSeasonTeamFragment;

    public EventPersonSportFragment(int competitionId, int seasonId, int compSeasonEventId, int personSportId, int clientId) {
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
        personSportFragment = DataFragmentUtil.getFilledDataFragment(
                new PersonSportFragment(competitionId, seasonId, personSportId, clientId), getCacheDataKey(), stat);

        CompSeasonPersonSportKey cspsKey = new CompSeasonPersonSportKey(new CompSeasonKey(competitionId, seasonId),
                personSportId);

        List<CompSeasonTeamPersonSportKey> cstpsKeys = new CompSeasonTeamPersonSportManager(stat)
                .getKeysForCompSeasonPerson(cspsKey);

        if (cstpsKeys.size() == 1) {
            int teamId = cstpsKeys.get(0).getSuperKey().getSpecificId();

            compSeasonTeamFragment = DataFragmentUtil.getFilledDataFragment(
                    new CompSeasonParticipantFragment(competitionId, seasonId, teamId, clientId), getCacheDataKey(), stat);
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
}
