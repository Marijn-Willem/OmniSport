package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.comparator.MatchActionMatch;
import com.sports.entity.comparator.MatchActionMatchSort;
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
    private List<TeamMatchAction> matchActionList;
    private int curMatchActionIndX;

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
    protected void setParticipantsAndMatches() throws SQLException {
        super.setParticipantsAndMatches();

        List<TeamMatchKey> teamMatchKeys = new ArrayList<>();

        for (TeamMatch teamMatch : h2HMatches)
            teamMatchKeys.add(new TeamMatchKey(cspk.getSuperKey(), teamMatch.getSpecificId()));

        TeamMatchActionManager mam = new TeamMatchActionManager(stat);
        matchActionList = mam.getMatchActionsMatches(teamMatchKeys,
                Arrays.asList(ActionType.actionTypeIdTry, ActionType.actionTypeIdPenaltyTry5,
                        ActionType.actionTypeIdPenaltyTry7));

        matchActionList.sort(new MatchActionMatch());

        int matchActionIndX = 0;

        while (matchActionIndX < matchActionList.size()) {
            int curMatchId = matchActionList.get(matchActionIndX).getTeamMatchId();

            int matchSort = 0;

            while (matchSort < h2HMatches.size() && h2HMatches.get(matchSort).getTeamMatchId() != curMatchId)
                matchSort++;

            TeamMatchAction curTeamMatchAction;

            while (matchActionIndX < matchActionList.size() && (curTeamMatchAction = matchActionList.get(matchActionIndX)).getTeamMatchId() == curMatchId) {
                curTeamMatchAction.setMatchSort(matchSort);
                matchActionIndX++;
            }
        }

        matchActionList.sort(new MatchActionMatchSort());
    }

    @Override
    protected void processSpecificSnapshot(int curMatchSort) {
        super.processSpecificSnapshot(curMatchSort);

        int triesHome = 0;
        int triesAway = 0;

        TeamMatchAction prevTeamMatchAction = null;
        TeamMatchAction curTeamMatchAction;

        while (curMatchActionIndX < matchActionList.size() &&
                (curTeamMatchAction = matchActionList.get(curMatchActionIndX)).getMatchSort() <= curMatchSort) {
            if (prevTeamMatchAction != null && prevTeamMatchAction.getMatchSort() != curTeamMatchAction.getMatchSort()) {
                processBonusPointsMatch(prevTeamMatchAction.getMatchSort(), triesHome, triesAway);

                triesHome = 0;
                triesAway = 0;
            }

            TeamMatch curMatch = h2HMatches.get(curTeamMatchAction.getMatchSort());

            if (curMatch.getParticipant1Id() == curTeamMatchAction.getTeamId())
                triesHome += curTeamMatchAction.getCount();
            else
                triesAway += curTeamMatchAction.getCount();

            prevTeamMatchAction = curTeamMatchAction;
            curMatchActionIndX++;
        }

        if (prevTeamMatchAction != null)
            processBonusPointsMatch(prevTeamMatchAction.getMatchSort(), triesHome, triesAway);
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

    private void processBonusPointsMatch(int curMatchSort, int triesHome, int triesAway) {
        TeamMatch curMatch = h2HMatches.get(curMatchSort);
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
}
