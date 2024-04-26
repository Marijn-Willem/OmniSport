package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.comparator.MatchActionMatch;
import com.sports.entity.comparator.MatchId;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.entity.manager.TeamMatchActionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RugbyStandingProcessor extends TeamStandingProcessor {
    private final CompSeason compSeason;

    public RugbyStandingProcessor(Statement stat, CompSeasonPhaseKey cspk, CompSeason compSeason) {
        super(stat, cspk, Sport.sportIdRugby);
        this.compSeason = compSeason;
    }

    @Override
    public int getPointsWin() {
        return 4;
    }

    @Override
    public int getPointsDraw() {
        return 2;
    }

    @Override
    protected void processSpecific() throws SQLException {
        super.processSpecific();

        int curMatchIndX = -1;
        int triesHome = 0;
        int triesAway = 0;

        List<TeamMatchKey> teamMatchKeys = new ArrayList<>();

        for (TeamMatch teamMatch : h2HMatches)
            teamMatchKeys.add(new TeamMatchKey(cspk.getSuperKey(), teamMatch.getSpecificId()));

        TeamMatchActionManager mam = new TeamMatchActionManager(stat);
        List<TeamMatchAction> matchActionList = mam.getMatchActionsMatches(teamMatchKeys,
                Arrays.asList(ActionType.actionTypeIdTry, ActionType.actionTypeIdPenaltyTry5,
                        ActionType.actionTypeIdPenaltyTry7));

        h2HMatches.sort(new MatchId());
        matchActionList.sort(new MatchActionMatch());

        if (!matchActionList.isEmpty())
            curMatchIndX = getIndexNextMatch(0, matchActionList.get(0).getTeamMatchId());

        for (TeamMatchAction matchAction : matchActionList) {
            if (matchAction.getTeamMatchId() != getTeamMatchFromIndex(h2HMatches, curMatchIndX).getTeamMatchId()) {
                processBonusPointsMatch(curMatchIndX, triesHome, triesAway);

                triesHome = 0;
                triesAway = 0;

                curMatchIndX = getIndexNextMatch(curMatchIndX, matchAction.getTeamMatchId());
            }

            if (getTeamMatchFromIndex(h2HMatches, curMatchIndX).getTeamHomeId() == matchAction.getTeamId())
                triesHome++;
            else
                triesAway++;
        }

        if (curMatchIndX > -1)
            processBonusPointsMatch(curMatchIndX, triesHome, triesAway);
    }

    @Override
    protected void processH2HMatch(TeamMatch h2HMatch) {
        super.processH2HMatch(h2HMatch);

        if (compSeason.getLossDiffBonus() != null && h2HMatch.getScore1_1() != null && h2HMatch.getScore1_2() != null) {
            Team teamHome = particMap.get(h2HMatch.getParticipant1Id());
            Team teamAway = particMap.get(h2HMatch.getParticipant2Id());

            int scoreDiff = h2HMatch.getScore1_1() - h2HMatch.getScore1_2();

            if (scoreDiff > 0 && scoreDiff <= compSeason.getLossDiffBonus()) {
                teamAway.addPoints(1);
                teamAway.addBonusPoint();
            } else if (scoreDiff < 0 && scoreDiff >= -compSeason.getLossDiffBonus()) {
                teamHome.addPoints(1);
                teamHome.addBonusPoint();
            }
        }
    }

    private void processBonusPointsMatch(int curMatchIndX, int triesHome, int triesAway) {
        TeamMatch curMatch = h2HMatches.get(curMatchIndX);
        int triesDiff = triesHome - triesAway;
        processBonusPoints(particMap.get(curMatch.getTeamHomeId()), triesHome, triesDiff);
        processBonusPoints(particMap.get(curMatch.getTeamAwayId()), triesAway, -triesDiff);
    }

    private void processBonusPoints(Team participant, int tries, int triesDiff) {
        if (compSeason.getTriesAbsBonus() != null && tries >= compSeason.getTriesAbsBonus()) {
            participant.addPoints(1);
            participant.addBonusPoint();
        }

        if (compSeason.getTriesRelBonus() != null && triesDiff >= compSeason.getTriesRelBonus()) {
            participant.addPoints(1);
            participant.addBonusPoint();
        }
    }

    private int getIndexNextMatch(int curIndX, int nextMatchId) {
        int nextIndX = curIndX;

        while (h2HMatches.get(nextIndX).getSpecificId() != nextMatchId)
            nextIndX++;

        return nextIndX;
    }

    private TeamMatch getTeamMatchFromIndex(List<TeamMatch> matches, int indX) {
        return matches.get(indX);
    }
}
