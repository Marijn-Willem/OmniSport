package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.StandingParticipantKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.calc.teamsports.Calculation;
import com.sports.entity.Participant;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;

public class StandingParticipantFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int participantId;
    private final int clientId;
    private final boolean isDomesticUSA;

    private final int rank;
    private ParticipantFragment participantFragment;
    private final int played;
    private final int wins;
    private final int draws;
    private final int losses;
    private final int points;
    private final int score;
    private final int scoreAgainst;
    private final int winsMain;
    private final double average;
    private final double averageConference;
    private final double averageDivision;
    private final int streak;
    private final double gamesBehind;

    public StandingParticipantFragment(CompSeasonKey compSeasonKey, Participant participant, int clientId,
                                       boolean isDomesticUSA) {
        this.competitionId = compSeasonKey.getCompetitionId();
        this.seasonId = compSeasonKey.getSeasonId();
        this.participantId = participant.getId();
        this.clientId = clientId;
        this.isDomesticUSA = isDomesticUSA;

        rank = participant.getRank();
        played = participant.getPlayed();
        wins = participant.getWins();
        draws = participant.getDraws();
        losses = participant.getLosses();
        points = participant.getPoints();
        score = participant.getScore();
        scoreAgainst = participant.getScoreAgainst();
        winsMain = participant.getWinsMain();
        average = participant.getAverage();
        averageConference = isDomesticUSA ? ((Team)participant).getParentDivisionAverage() : 0.0;
        averageDivision = isDomesticUSA ? ((Team)participant).getDivisionAverage() : 0.0;
        streak = participant.getStreak();
        gamesBehind = participant.getGamesBehind();
    }

    @Override
    public CacheKey getCacheKey() {
        return new StandingParticipantKey(competitionId, seasonId, participantId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        participantFragment = DataFragmentUtil.getFilledDataFragment(
                new ParticipantFragment(competitionId, seasonId, participantId, clientId), getCacheDataKey(), stat);
    }

    @Override
    public String toXML() {
        return isDomesticUSA ? toXMLUSA() : toXMLGeneral();
    }

    @Override
    public String toJson() {
        return isDomesticUSA ? toJsonUSA() : toJsonGeneral();
    }

    private String toXMLGeneral() {
        return XmlUtil.getTag("rank", rank) +
                participantFragment.toXML() +
                XmlUtil.getTag("played", played) +
                XmlUtil.getTag("wins", wins) +
                XmlUtil.getTag("draws", draws) +
                XmlUtil.getTag("losses", losses) +
                XmlUtil.getTag("points", points) +
                XmlUtil.getTag("score", score) +
                XmlUtil.getTag("scoreAgainst", scoreAgainst) +
                XmlUtil.getTag("winsMain", winsMain);
    }

    private String toJsonGeneral() {
        return JsonUtil.getEntry("rank", rank) + "," +
                participantFragment.toJson() + "," +
                JsonUtil.getEntry("played", played) + "," +
                JsonUtil.getEntry("wins", wins) + "," +
                JsonUtil.getEntry("draws", draws) + "," +
                JsonUtil.getEntry("losses", losses) + "," +
                JsonUtil.getEntry("points", points) + "," +
                JsonUtil.getEntry("score", score) + "," +
                JsonUtil.getEntry("scoreAgainst", scoreAgainst) + "," +
                JsonUtil.getEntry("winsMain", winsMain);
    }

    private String toXMLUSA() {
        return XmlUtil.getTag("rank", rank) +
                participantFragment.toXML() +
                XmlUtil.getTag("played", played) +
                XmlUtil.getTag("average", Util.getDoubleAsStringWith3Digits(average)) +
                XmlUtil.getTag("averageConference", Util.getDoubleAsStringWith3Digits(averageConference)) +
                XmlUtil.getTag("averageDivision", Util.getDoubleAsStringWith3Digits(averageDivision)) +
                XmlUtil.getTag("streak", Calculation.getStreakAsString(streak)) +
                XmlUtil.getTag("gamesBehind", Util.getDoubleAsStringWith2Digits(gamesBehind));
    }

    private String toJsonUSA() {
        return JsonUtil.getEntry("rank", rank) + "," +
                participantFragment.toJson() + "," +
                JsonUtil.getEntry("played", played) + "," +
                JsonUtil.getEntry("average", Util.getDoubleAsStringWith3Digits(average)) + "," +
                JsonUtil.getEntry("averageConference", Util.getDoubleAsStringWith3Digits(averageConference)) + "," +
                JsonUtil.getEntry("averageDivision", Util.getDoubleAsStringWith3Digits(averageDivision)) + "," +
                JsonUtil.getEntry("streak", Calculation.getStreakAsString(streak)) + "," +
                JsonUtil.getEntry("gamesBehind", Util.getDoubleAsStringWith2Digits(gamesBehind));
    }
}
