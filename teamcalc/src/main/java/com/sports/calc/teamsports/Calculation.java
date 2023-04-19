package com.sports.calc.teamsports;

import com.sports.entity.Team;
import com.sports.entity.TeamMatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculation {
    public static List<Team> getStandingFromVirtualScores(List<TeamMatch> teamMatchList, List<Team> teamList) {
        Map<Integer, Team> teamMap = new HashMap<Integer, Team>();
        List<Team> newStanding = new ArrayList<Team>();

        for (Team team : teamList) {
            Team newTeam = new Team();
            newTeam.setId(team.getId());
            newTeam.setDescription(team.getDescription());
            newTeam.setPlayed(team.getPlayed());
            newTeam.setPoints(team.getPoints());
            newTeam.setWins(team.getWins());
            newTeam.setDraws(team.getDraws());
            newTeam.setLosses(team.getLosses());
            newTeam.setScore(team.getScore());
            newTeam.setScoreAgainst(team.getScoreAgainst());

            newStanding.add(newTeam);
            teamMap.put(team.getId(), newTeam);
        }

        for (TeamMatch teamMatch : teamMatchList) {
            Team teamHome = teamMap.get(teamMatch.getTeamHomeId());
            Team teamAway = teamMap.get(teamMatch.getTeamAwayId());

            teamHome.addScore(teamMatch.getScoreHomeVirtual() - teamMatch.getScoreHome());
            teamAway.addScore(teamMatch.getScoreAwayVirtual() - teamMatch.getScoreAway());

            teamHome.addScoreAgainst(teamMatch.getScoreAwayVirtual() - teamMatch.getScoreAway());
            teamAway.addScoreAgainst(teamMatch.getScoreHomeVirtual() - teamMatch.getScoreHome());

            int scoreDiffActual = teamMatch.getScoreHome() - teamMatch.getScoreAway();
            int scoreDiffVirtual = teamMatch.getScoreHomeVirtual() - teamMatch.getScoreAwayVirtual();

            updateTeamStandingFields(scoreDiffActual, scoreDiffVirtual, teamHome);
            updateTeamStandingFields(-scoreDiffActual, -scoreDiffVirtual, teamAway);
        }

        com.sports.logic.calculation.Calculation.sortParticipantsAndSetRankBasedFields(newStanding);

        return newStanding;
    }

    public static String getStreakAsString(int streak) {
        if (streak == 0)
            return "-";

        String prefix = streak < 0 ? "L" : "W";

        return prefix + Math.abs(streak);
    }

    private static void updateTeamStandingFields(int scoreDiffActual, int scoreDiffVirtual, Team team) {
        int sgnActual = (int)Math.signum(scoreDiffActual);
        int sgnVirtual = (int)Math.signum(scoreDiffVirtual);

        if (sgnActual != sgnVirtual) {
            if (sgnVirtual == 1) {
                team.addWin();
                team.addPoints(3);

                if (sgnActual == 0) {
                    team.removeDraw();
                    team.addPoints(-1);
                }
                else {
                    team.removeLoss();
                }
            }
            else if (sgnVirtual == 0) {
                team.addDraw();
                team.addPoints(1);

                if (sgnActual == 1) {
                    team.removeWin();
                    team.addPoints(-3);
                }
                else {
                    team.removeLoss();
                }
            }
            else {
                team.addLoss();

                if (sgnActual == 1) {
                    team.removeWin();
                    team.addPoints(-3);
                }
                else {
                    team.removeDraw();
                    team.addPoints(-1);
                }
            }
        }
    }
}
