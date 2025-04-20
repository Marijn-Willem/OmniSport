package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.util.Util;

public abstract class Participant extends IntEntity implements DescribedEntity {
    private static final int eloDefault = 1500;

    private String description;
    private int elo = eloDefault;

    private int id;
    private Integer points;
    private Integer pointsBehind;
    private Integer noCountResultId;
    private int winsMain;
    private int wins;
    private int draws;
    private int losses;
    private int score;
    private int scoreAgainst;
    private int played;
    private boolean isNewlyCreated;
    private Integer rank;
    private int streak;
    private double gamesBehind;
    private double resultPoints;

    abstract String[] getSpecificPropertiesInSQLStrings();

    @Override
    public String[] getPropertiesInSQLStrings() {
        String[] generalProps = new String[] {
                QueryUtil.convertStringToDbValue(description),
                String.valueOf(elo)
            };

        return Util.concatenateStringArrays(generalProps, getSpecificPropertiesInSQLStrings());
    }

    void copyStandingFieldsTo(Participant participant) {
        participant.setDescription(description);
        participant.setElo(elo);

        participant.setId(id);
        participant.setPoints(points);
        participant.setPointsBehind(pointsBehind);
        participant.setNoCountResultId(noCountResultId);
        participant.setWinsMain(winsMain);
        participant.setWins(wins);
        participant.setDraws(draws);
        participant.setLosses(losses);
        participant.setScore(score);
        participant.setScoreAgainst(scoreAgainst);
        participant.setPlayed(played);
        participant.setRank(rank);
        participant.setStreak(streak);
        participant.setGamesBehind(gamesBehind);
    }

    public double getAverage() {
        return Calculation.getAverage(played, wins, draws);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void addPoints(int points) {
        if (this.points == null)
            this.points = 0;

        this.points += points;
    }

    public Integer getPointsBehind() {
        return pointsBehind;
    }

    public void setPointsBehind(Integer pointsBehind) {
        this.pointsBehind = pointsBehind;
    }

    public Integer getNoCountResultId() {
        return noCountResultId;
    }

    public void setNoCountResultId(Integer noCountResultId) {
        this.noCountResultId = noCountResultId;
    }

    public int getWinsMain() {
        return winsMain;
    }

    public void setWinsMain(int winsMain) {
        this.winsMain = winsMain;
    }

    public void addWinMain() {
        winsMain++;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addWin() {
        wins++;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void addDraw() {
        draws++;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void addLoss() {
        losses++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScoreAgainst() {
        return scoreAgainst;
    }

    public void setScoreAgainst(int scoreAgainst) {
        this.scoreAgainst = scoreAgainst;
    }

    public void addScoreAgainst(int scoreAgainst) {
        this.scoreAgainst += scoreAgainst;
    }

    public int getScoreDiff() {
        return score - scoreAgainst;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public void addPlayed(int played) {
        this.played += played;
    }

    public boolean isNewlyCreated() {
        return isNewlyCreated;
    }

    public void setNewlyCreated(boolean newlyCreated) {
        isNewlyCreated = newlyCreated;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public double getGamesBehind() {
        return gamesBehind;
    }

    public void setGamesBehind(double gamesBehind) {
        this.gamesBehind = gamesBehind;
    }

    public void addResultPoints(double resultPoints) {
        this.resultPoints += resultPoints;
    }

    public double getResultPoints() {
        return resultPoints;
    }
}
