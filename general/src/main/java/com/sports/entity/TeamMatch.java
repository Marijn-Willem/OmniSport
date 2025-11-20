package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.manager.*;
import com.sports.logic.util.Util;

import java.sql.Statement;

public class TeamMatch extends H2HMatch {
    private Integer teamHomeId;
    private Integer teamAwayId;
    private Integer scoreHome;
    private Integer scoreAway;
    private Integer scoreShootoutHome;
    private Integer scoreShootoutAway;
    private Integer teamHomeNcrId;
    private Integer teamAwayNcrId;

    private int competitionId;
    private int seasonId;
    private int teamMatchId;

    @Override
    String[] getSpecificPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(scoreShootoutHome),
                QueryUtil.convertIntegerToDbValue(scoreShootoutAway)
            };
    }

    @Override
    void copySpecific(H2HMatch other) {
        ((TeamMatch) other).scoreShootoutHome = this.scoreShootoutHome;
        ((TeamMatch) other).scoreShootoutAway = this.scoreShootoutAway;
    }

    @Override
    void addScoresFromSpecific(H2HMatch matchFrom) {
        TeamMatch matchFromCast = (TeamMatch) matchFrom;

        Integer scoreShootoutHomeFrom = getParticipant1Id().equals(matchFromCast.getParticipant1Id()) ?
                matchFromCast.getScoreShootoutHome() : matchFromCast.getScoreShootoutAway();
        Integer scoreShootoutAwayFrom = getParticipant1Id().equals(matchFromCast.getParticipant1Id()) ?
                matchFromCast.getScoreShootoutAway() : matchFromCast.getScoreShootoutHome();

        if (scoreShootoutHomeFrom != null)
            scoreShootoutHome = Util.convertEmptyIntegerToZero(scoreShootoutHome) + scoreShootoutHomeFrom;

        if (scoreShootoutAwayFrom != null)
            scoreShootoutAway = Util.convertEmptyIntegerToZero(scoreShootoutAway) + scoreShootoutAwayFrom;
    }

    public TeamMatchManager getManager(Statement stat) {
        return new TeamMatchManager(stat);
    }

    public TeamManager getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    public CompSeasonPhaseTeamManager getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseTeamManager(stat);
    }

    void setSpecificId(int specificId) { teamMatchId = specificId; }

    public int getSpecificId() {
        return teamMatchId;
    }

    public void setParticipant1Id(Integer participant1id) {
        teamHomeId = participant1id;
    }

    public Integer getParticipant1Id() {
        return teamHomeId;
    }

    public void setParticipant2Id(Integer participant2id) {
        teamAwayId = participant2id;
    }

    public Integer getParticipant2Id() {
        return teamAwayId;
    }

    public void setParticipant1Start(boolean participant1Start) {

    }

    public boolean isParticipant1Start() {
        return false;
    }

    public void setScore1_1(Integer score1_1) {
        setScoreHome(score1_1);
    }

    public Integer getScore1_1() {
        return getScoreHome();
    }

    public void setScore1_2(Integer score1_2) {
        setScoreAway(score1_2);
    }

    public Integer getScore1_2() {
        return getScoreAway();
    }

    public Integer getTeamHomeNcrId() {
        return teamHomeNcrId;
    }

    public void setTeamHomeNcrId(Integer teamHomeNcrId) {
        this.teamHomeNcrId = teamHomeNcrId;
    }

    public Integer getTeamAwayNcrId() {
        return teamAwayNcrId;
    }

    public void setTeamAwayNcrId(Integer teamAwayNcrId) {
        this.teamAwayNcrId = teamAwayNcrId;
    }

    @Override
    public Integer getParticipant1NcrId() {
        return getTeamHomeNcrId();
    }

    @Override
    public void setParticipant1NcrId(Integer participant1NcrId) {
        setTeamHomeNcrId(participant1NcrId);
    }

    @Override
    public Integer getParticipant2NcrId() {
        return getTeamAwayNcrId();
    }

    @Override
    public void setParticipant2NcrId(Integer participant2NcrId) {
        setTeamAwayNcrId(participant2NcrId);
    }

    @Override
    public boolean isParticipant1Win() {
        return super.isParticipant1Win() || (
                scoreShootoutHome != null && scoreShootoutAway != null &&
                        scoreShootoutHome > scoreShootoutAway
        );
    }

    @Override
    public boolean isParticipant2Win() {
        return super.isParticipant2Win() || (
                scoreShootoutHome != null && scoreShootoutAway != null &&
                        scoreShootoutAway > scoreShootoutHome
        );
    }

    public TeamMatchKey getTeamMatchKey() {
        return new TeamMatchKey(new CompSeasonKey(competitionId, seasonId), teamMatchId);
    }

    public Integer getTeamHomeId() {
        return teamHomeId;
    }

    public Integer getTeamAwayId() {
        return teamAwayId;
    }

    public Integer getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(Integer scoreHome) {
        this.scoreHome = scoreHome;
    }

    public Integer getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(Integer scoreAway) {
        this.scoreAway = scoreAway;
    }

    public Integer getScoreShootoutHome() {
        return scoreShootoutHome;
    }

    public void setScoreShootoutHome(Integer scoreShootoutHome) {
        this.scoreShootoutHome = scoreShootoutHome;
    }

    public Integer getScoreShootoutAway() {
        return scoreShootoutAway;
    }

    public void setScoreShootoutAway(Integer scoreShootoutAway) {
        this.scoreShootoutAway = scoreShootoutAway;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getTeamMatchId() {
        return teamMatchId;
    }

    public void setTeamMatchId(int teamMatchId) {
        this.teamMatchId = teamMatchId;
    }
}
