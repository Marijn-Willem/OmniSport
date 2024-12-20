package com.sports.logic.calculation;

import com.sports.entity.*;
import com.sports.entity.comparator.CompSeasonPhaseTeamCorrectionDate;
import com.sports.entity.comparator.ParticipantStandingUSA;
import com.sports.entity.comparator.SuperComparator;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.factory.CompSeasonTeamFactory;
import com.sports.logic.util.Util;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamStandingProcessor extends StandingProcessor<CompSeasonTeamKey,
        CompSeasonPhaseTeamKey,
        Team,
        CompSeasonTeam,
        TeamMatchKey,
        TeamMatch,
        TeamMatchPartKey,
        TeamMatchPart,
        H2HMatchPartStatKey,
        H2HMatchPartStat> {
    private final int sportId;

    private final Map<Integer, CompSeasonTeam> compSeasonTeamMap = new HashMap<>();
    private final Map<Integer, CompDivision> compDivisionMap = new HashMap<>();
    private final Map<Integer, List<CompSeasonPhaseTeamCorrection>> teamCorrectionMap = new HashMap<>();

    public TeamStandingProcessor(Statement stat, CompSeasonPhaseKey cspk, int sportId) {
        super(stat, cspk);
        this.sportId = sportId;
    }

    @Override
    protected void processSpecificSnapshot(int curMatchSort) {
        LocalDateTime matchDate = h2HMatches.get(curMatchSort).getDate();

        if (matchDate != null)
            for (Map.Entry<Integer, List<CompSeasonPhaseTeamCorrection>> me : teamCorrectionMap.entrySet()) {
                List<CompSeasonPhaseTeamCorrection> correctionsUsed = new ArrayList<>();
                me.getValue().forEach(x -> {
                    if (!x.getDate().isAfter(matchDate)) {
                        particMap.get(me.getKey()).addPoints(x.getPointsCorrection());
                        correctionsUsed.add(x);
                    }
                });

                correctionsUsed.forEach(x -> me.getValue().remove(x));
            }
    }

    @Override
    protected void setParticipantsAndMatches() throws SQLException {
        super.setParticipantsAndMatches();

        CompSeasonKey compSeasonKey = cspk.getSuperKey();
        new CompSeasonTeamManager(stat).getTeamsInCompSeason(compSeasonKey).forEach(x ->
                compSeasonTeamMap.put(x.getTeamId(), x));

        List<CompDivisionKey> compDivisionKeys = new CompSeasonDivisionManager(stat)
                .getCompSeasonDivisions(cspk.getSuperKey()).stream()
                .map(x -> new CompDivisionKey(compSeasonKey.getCompetitionId(), x.getCompDivisionId()))
                .toList();
        new CompDivisionManager(stat).getCompDivisionList(compDivisionKeys).forEach(x ->
                compDivisionMap.put(x.getCompDivisionId(), x));

        CompSeasonPhase compSeasonPhase = new CompSeasonPhaseManager(stat).getCompSeasonPhase(cspk);

        if (compSeasonPhase.isHasDivisionStandings())
            new DbCalculation(stat).addCompDivisionsToTeams(particMap.values().stream().toList(), cspk.getSuperKey());

        List<CompSeasonPhaseTeamCorrection> correctionList = new CompSeasonPhaseTeamCorrectionManager(stat)
                .getCorrectionsForCompSeasonPhase(cspk);

        correctionList.sort(new CompSeasonPhaseTeamCorrectionDate());

        correctionList.forEach(x -> {
            int teamId = x.getTeamId();

            if (!teamCorrectionMap.containsKey(teamId))
                teamCorrectionMap.put(teamId, new ArrayList<>());

            teamCorrectionMap.get(teamId).add(x);
        });
    }

    @Override
    protected void processH2HMatch(TeamMatch h2HMatch) {
        Team teamHome = particMap.get(h2HMatch.getParticipant1Id());
        Team teamAway = particMap.get(h2HMatch.getParticipant2Id());

        Integer oldWinsHome = teamHome != null ? teamHome.getWins() : null;
        Integer oldWinsAway = teamAway != null ? teamAway.getWins() : null;
        Integer oldDrawsHome = teamHome != null ? teamHome.getDraws() : null;
        Integer oldDrawsAway = teamAway != null ? teamAway.getDraws() : null;

        super.processH2HMatch(h2HMatch);

        CompSeasonTeam compSeasonTeamHome = compSeasonTeamMap.get(h2HMatch.getParticipant1Id());
        CompSeasonTeam compSeasonTeamAway = compSeasonTeamMap.get(h2HMatch.getParticipant2Id());

        Integer compDivisionHomeId = compSeasonTeamHome != null ? compSeasonTeamHome.getCompDivisionId() : null;
        Integer compDivisionAwayId = compSeasonTeamAway != null ? compSeasonTeamAway.getCompDivisionId() : null;

        if (Util.compareIntegersNotNull(compDivisionHomeId, compDivisionAwayId)) {
            if (teamHome != null)
                processDivisionFields(teamHome, oldWinsHome, oldDrawsHome);

            if (teamAway != null)
                processDivisionFields(teamAway, oldWinsAway, oldDrawsAway);
        }

        CompDivision compDivisionHome = compDivisionHomeId != null ? compDivisionMap.get(compDivisionHomeId) : null;
        CompDivision compDivisionAway = compDivisionAwayId != null ? compDivisionMap.get(compDivisionAwayId) : null;

        if (compDivisionHome != null && compDivisionAway != null &&
                Util.compareIntegersNotNull(
                        compDivisionHome.getParentDivisionId(), compDivisionAway.getParentDivisionId())) {
            if (teamHome != null)
                processParentDivisionFields(teamHome, oldWinsHome, oldDrawsHome);

            if (teamAway != null)
                processParentDivisionFields(teamAway, oldWinsAway, oldDrawsAway);
        }
    }

    @Override
    public int getPointsWin() {
        return switch (sportId) {
            case Sport.sportIdFootball, Sport.sportIdHockey -> 3;
            default -> super.getPointsWin();
        };
    }

    @Override
    protected CompSeasonTeamFactory getFactory() {
        return new CompSeasonTeamFactory();
    }

    @Override
    protected SuperComparator<Team> getComparator() throws SQLException {
        Competition competition = new CompetitionManager(stat).getCompetition(cspk.getCompetitionId());

        if (competition.isDomestic() && competition.getGeoId() == Geo.geoIdUSA)
            return new ParticipantStandingUSA();

        return super.getComparator();
    }

    private void processDivisionFields(Team team, int oldWins, int oldDraws) {
        team.addPlayedDivision();

        if (team.getWins() > oldWins)
            team.addWinDivision();

        if (team.getDraws() > oldDraws)
            team.addDrawDivision();
    }

    private void processParentDivisionFields(Team team, int oldWins, int oldDraws) {
        team.addPlayedParentDivision();

        if (team.getWins() > oldWins)
            team.addWinParentDivision();

        if (team.getDraws() > oldDraws)
            team.addDrawParentDivision();
    }
}
