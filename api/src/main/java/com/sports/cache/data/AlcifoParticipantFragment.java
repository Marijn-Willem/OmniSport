package com.sports.cache.data;

import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.alcifo.Calculation;
import com.sports.entity.Participant;
import com.sports.entity.PersonSport;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonKey;

import java.sql.SQLException;
import java.sql.Statement;

public abstract class AlcifoParticipantFragment extends WritableFragment {
    final int competitionId;
    final int seasonId;
    final int participantId;
    private final int clientId;
    private final Integer rank;
    private final Integer points;
    private final Integer pointsBehind;
    private final Integer noCountResultId;
    private final int resultTypeId;
    private final Integer resultTypePrecisionId;

    abstract String getDescription(Statement stat) throws SQLException;

    private String description;
    private NoCountResultFragment noCountResultFragment;

    public AlcifoParticipantFragment(int competitionId, int seasonId, Participant participant, int resultTypeId,
                                     Integer resultTypePrecisionId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        participantId = participant.getId();
        rank = participant.getRank();
        points = participant.getPoints();
        pointsBehind = participant.getPointsBehind();
        noCountResultId = participant.getNoCountResultId();
        this.resultTypeId = resultTypeId;
        this.resultTypePrecisionId = resultTypePrecisionId;
        this.clientId = clientId;
    }

    @Override
    void fill(Statement stat) throws SQLException {
        description = getDescription(stat);
        if (noCountResultId != null)
            noCountResultFragment = DataFragmentUtil.getFilledDataFragment(new NoCountResultFragment(noCountResultId),
                    getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("id", participantId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("rank", rank) +
                XmlUtil.getTag("points", getPointsString(points)) +
                XmlUtil.getTag("pointsBehind", getPointsString(pointsBehind)) +
                XmlUtil.getNullableFragmentAsTag("noCountResult", noCountResultFragment);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("id", participantId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("rank", rank) + "," +
                JsonUtil.getEntry("points", getPointsString(points)) + "," +
                JsonUtil.getEntry("pointsBehind", getPointsString(pointsBehind)) + "," +
                JsonUtil.getNullableFragmentAsEntry("noCountResult", noCountResultFragment);
    }

    String getDescriptionPersonSport(int personId, Statement stat) throws SQLException {
        PersonSport personSport = new PersonSport();
        personSport.setPersonId(personId);

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        return new DescribedEntityUtil(clientId, getCacheDataKey(), stat).getPersonSportString(personSport, compSeasonKey);
    }

    String getDescriptionTeam(Integer clubId, Integer nocId, Integer equipeId, Statement stat) throws SQLException {
        Team team = new Team();
        team.setClubId(clubId);
        team.setNocId(nocId);
        team.setEquipeId(equipeId);

        CompSeasonKey compSeasonKey = new CompSeasonKey(competitionId, seasonId);

        return new DescribedEntityUtil(clientId, getCacheDataKey(), stat).getTeamString(team, compSeasonKey);
    }

    private String getPointsString(Integer points) {
        return points != null ? Calculation.getPointsAsString(resultTypeId, resultTypePrecisionId, points) : null;
    }
}
