package com.sports.calc.teamsports;

import com.sports.entity.*;
import com.sports.entity.comparator.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;
import com.sports.logic.calculation.Calculation;
import com.sports.logic.calculation.StandingContext;
import com.sports.logic.util.Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record DbCalculation(Statement stat) {
    public List<Team> getStandingCompSeasonPhase(CompSeasonPhaseKey cspk)
            throws SQLException {
        return new com.sports.logic.calculation.DbCalculation(stat).getParticipantStandingCompSeasonPhase(cspk)
                .stream().map(x -> (Team)x).toList();
    }

    public List<Team> getDivisionStanding(CompSeasonPhaseKey cspk, CompDivisionKey csd) throws SQLException {
        int sportId = new CompetitionManager(stat).getCompetition(cspk.getCompetitionId()).getSportId();
        return new DivisionStandingProcessor(stat, cspk, sportId, csd).getStanding().standing();
    }

    public List<StandingContext<Team>> getDivisionStandingPerDate(CompSeasonPhaseKey cspk, CompDivisionKey csd)
            throws SQLException {
        int sportId = new CompetitionManager(stat).getCompetition(cspk.getCompetitionId()).getSportId();
        return new DivisionStandingProcessor(stat, cspk, sportId, csd).getStandingsPerDate();
    }

    public List<Team> getCrocoCupStanding(int competitionId) throws SQLException {
        List<Team> standing = new ArrayList<>();

        Competition competition = new CompetitionManager(stat).getCompetition(competitionId);

        if (competition != null && competition.getCupTeamInitId() != null && competition.getCupDateInit() != null) {
            Map<Integer, Team> teamMapProc = new HashMap<>();

            List<CompSeasonPhaseKey> compSeasonPhaseKeys = new CompSeasonPhaseManager(stat)
                    .getCompSeasonPhasesAfterDate(Collections.singletonList(competitionId),
                            competition.getCupDateInit());

            List<CompSeasonPhaseTeamKey> compSeasonPhaseTeamKeys =
                    getCompSeasonPhaseTeamKeys(compSeasonPhaseKeys, competition.getCupTeamInitId());

            TeamMatchManager mm = new TeamMatchManager(stat);

            List<TeamMatch> teamMatches = mm.getPlayedMatchesTeamAfterDate(compSeasonPhaseTeamKeys,
                    competition.getCupDateInit());

            MatchDate matchDtComp = new MatchDate();

            teamMatches.sort(matchDtComp);

            int curTeamId = competition.getCupTeamInitId();
            int indX = 0;

            while (indX < teamMatches.size()) {
                TeamMatch teamMatch = teamMatches.get(indX);

                if ((teamMatch.getTeamHomeId() == curTeamId && teamMatch.getScoreHome() >= teamMatch.getScoreAway()) ||
                        (teamMatch.getTeamAwayId() == curTeamId && teamMatch.getScoreHome() <= teamMatch.getScoreAway()))
                    indX++;
                else {
                    if (teamMapProc.containsKey(curTeamId))
                        teamMapProc.get(curTeamId).setHasCup(false); // No creation

                    curTeamId = teamMatch.getTeamHomeId() == curTeamId ? teamMatch.getTeamAwayId()
                            : teamMatch.getTeamHomeId();

                    compSeasonPhaseTeamKeys = getCompSeasonPhaseTeamKeys(compSeasonPhaseKeys, curTeamId);
                    LocalDateTime nextDay = Util.cloneDateTimeIgnoreTime(teamMatch.getDate()).plusDays(1);

                    teamMatches = mm.getPlayedMatchesTeamAfterDate(compSeasonPhaseTeamKeys, nextDay);
                    teamMatches.sort(matchDtComp);

                    indX = 0;
                }

                Team team = getOrCreateTeamFromMap(teamMapProc, curTeamId);
                team.setHasCup(true);
                team.addPoints(1);
            }

            Map<Integer, Team> teamMap = new TeamManager(stat).getTeamMap(teamMapProc.keySet());

            for (Map.Entry<Integer, Team> me : teamMap.entrySet()) {
                Team team = me.getValue();
                Team teamProc = teamMapProc.get(me.getKey());

                team.setHasCup(teamProc.isHasCup());
                team.setPoints(teamProc.getPoints());

                standing.add(team);
            }

            Calculation.sortParticipantsAndSetRankBasedFields(standing);
        }

        return standing;
    }

    public List<Integer> getTeamsIdsInCompDivision(CompSeasonKey csk, CompDivisionKey cdk)
            throws SQLException {
        List<CompSeasonDivisionKey> divisionKeys = new ArrayList<>();

        CompDivisionManager csdm = new CompDivisionManager(stat);
        CompDivision cd = csdm.getCompDivision(cdk);

        if (cd.getParentDivisionId() == null) {
            List<CompDivision> childDivisions = csdm.getChildDivisions(cdk);

            for (CompDivision childDivision : childDivisions)
                divisionKeys.add(new CompSeasonDivisionKey(csk, childDivision.getCompDivisionId()));
        } else
            divisionKeys.add(new CompSeasonDivisionKey(csk, cd.getCompDivisionId()));

        List<CompSeasonTeamKey> compSeasonTeamKeys =
                new CompSeasonTeamManager(stat).getTeamsInCompSeasonDivision(divisionKeys);

        List<Integer> teamIds = new ArrayList<>();

        for (CompSeasonTeamKey compSeasonTeamKey : compSeasonTeamKeys)
            teamIds.add(compSeasonTeamKey.getSpecificId());

        return teamIds;
    }

    public List<CompDivision> getSortedCompDivisions(CompSeasonKey csk) throws SQLException {
        List<CompSeasonDivisionKey> csdKeys = new CompSeasonDivisionManager(stat).getCompSeasonDivisions(csk);
        List<CompDivisionKey> cdKeyes = csdKeys.stream().map(csdk ->
                new CompDivisionKey(csdk.getSuperKey().getCompetitionId(), csdk.getCompDivisionId())
        ).collect(Collectors.toList());

        List<CompDivision> compDivisions = new CompDivisionManager(stat).getCompDivisionList(cdKeyes);
        List<CompDivision> parentDivisions = new ArrayList<>();
        List<CompDivision> childDivisions = new ArrayList<>();

        for (CompDivision compDivision : compDivisions)
            if (compDivision.getParentDivisionId() == null)
                parentDivisions.add(compDivision);
            else
                childDivisions.add(compDivision);

        parentDivisions.sort(new NamedEntityName());
        setParentDivisionSorts(parentDivisions, childDivisions);

        // Also include parent divisions in the result for conference only competitions
        parentDivisions.addAll(childDivisions);
        parentDivisions.sort(new CompDivisionParentSort());

        return parentDivisions;
    }

    public void importMatchActions(int sportId, String fileName) throws IOException, SQLException {
        List<MatchActionImportItem> importItems = Files.readAllLines(FileSystems.getDefault().getPath(fileName + ".txt"),
                StandardCharsets.ISO_8859_1).stream().map(this::getMatchActionImportItem).toList();

        Map<String, Competition> competitionMap = new HashMap<>() {{
            new CompetitionManager(stat).getCompetitionList(sportId).forEach(x -> put(x.getName(), x));
        }};

        Map<Integer, Competition> competitionIdMap = new HashMap<>() {{
            competitionMap.forEach((k, v) -> put(v.getId(), v));
        }};

        List<LocalDateTime> dateTimes = importItems.stream().map(MatchActionImportItem::getDate)
                .collect(Collectors.toList());

        List<TeamMatch> teamMatches = new TeamMatchManager(stat).getMatchesByCompetitionsAndDates(
                new ArrayList<>(competitionIdMap.keySet()), dateTimes);

        Map<Integer, List<MatchActionImportItem>> genderImportItemMap = new HashMap<>();
        Map<Integer, List<TeamMatch>> genderTeamMatchMap = new HashMap<>();

        importItems.forEach(ii -> {
            int genderId = competitionMap.get(ii.getCompetition()).getGenderId();

            if (!genderImportItemMap.containsKey(genderId))
                genderImportItemMap.put(genderId, new ArrayList<>());

            genderImportItemMap.get(genderId).add(ii);
        });

        teamMatches.forEach(m -> {
            int genderId = competitionIdMap.get(m.getTeamMatchKey().getCompetitionId()).getGenderId();

            if (!genderTeamMatchMap.containsKey(genderId))
                genderTeamMatchMap.put(genderId, new ArrayList<>());

            genderTeamMatchMap.get(genderId).add(m);
        });

        Map<TeamMatchActionKey, TeamMatchAction> matchActionMap = new HashMap<>();

        for (Map.Entry<Integer, List<MatchActionImportItem>> me : genderImportItemMap.entrySet())
            importMatchActionsForSportAndGender(sportId, matchActionMap, me.getKey(),
                    me.getValue(), genderTeamMatchMap.get(me.getKey()));

        new TeamMatchActionManager(stat).addMatchActions(matchActionMap);
    }

    private List<CompSeasonPhaseTeamKey> getCompSeasonPhaseTeamKeys(List<CompSeasonPhaseKey> compSeasonPhaseKeys,
                                                                    int clubId) {
        List<CompSeasonPhaseTeamKey> compSeasonPhaseTeamKeys = new ArrayList<>();

        for (CompSeasonPhaseKey compSeasonPhaseKey : compSeasonPhaseKeys)
            compSeasonPhaseTeamKeys.add(new CompSeasonPhaseTeamKey(compSeasonPhaseKey, clubId));

        return compSeasonPhaseTeamKeys;
    }

    private Team getOrCreateTeamFromMap(Map<Integer, Team> teamMap, int teamId) {
        if (!teamMap.containsKey(teamId))
            teamMap.put(teamId, new Team());

        return teamMap.get(teamId);
    }

    private void setParentDivisionSorts(List<CompDivision> parentDivisions, List<CompDivision> childDivisions) {
        for (CompDivision childDivision : childDivisions)
            for (int i = 0; i < parentDivisions.size(); i++)
                if (childDivision.getParentDivisionId() == parentDivisions.get(i).getCompDivisionId()) {
                    childDivision.setParentDivisionSort(i + 1);
                    break;
                }
    }

    private MatchActionImportItem getMatchActionImportItem(String line) {
        String[] lineParts = line.split("\\|");

        MatchActionImportItem importItem = new MatchActionImportItem();
        importItem.setCompetition(lineParts[0]);
        importItem.setTeamHome(lineParts[1]);
        importItem.setTeamAway(lineParts[2]);
        importItem.setDate(Util.convertStringToDateTime(lineParts[3]));
        importItem.setTeam(lineParts[4]);
        importItem.setActionType(lineParts[5]);
        importItem.setNr(Integer.parseInt(lineParts[6]));

        return importItem;
    }

    private void importMatchActionsForSportAndGender(int sportId, Map<TeamMatchActionKey, TeamMatchAction> matchActionMap,
                                                     int genderId, List<MatchActionImportItem> importItems,
                                                     List<TeamMatch> teamMatches) throws SQLException {
        List<String> teamNames = new ArrayList<>() {{
            importItems.forEach(x -> {
                add(x.getTeamHome());
                add(x.getTeamAway());
            });
        }};

        Map<String, Team> teamMap = new TeamManager(stat).getDescrTeamMapBySportGender(teamNames, sportId, genderId);

        importItems.forEach(ii -> {
            ii.setParticipant1Id(teamMap.get(ii.getTeamHome()).getId());
            ii.setParticipant2Id(teamMap.get(ii.getTeamAway()).getId());
        });

        Comparator<WithH2HMatchParticipantsDate> comparator = new H2HMatchParticipantsDate();

        importItems.sort(comparator);
        teamMatches.sort(comparator);

        TeamMatchActionManager tmam = new TeamMatchActionManager(stat);

        int[] indices = getNextIndices(teamMatches, importItems, 0, 0, comparator);
        int actionId = 0;

        if (indices[0] > -1)
            actionId = tmam.getNewTeamMatchActionId(teamMatches.get(indices[0]).getTeamMatchKey());

        while (indices[0] > -1 && indices[1] > -1) {
            TeamMatch teamMatch = teamMatches.get(indices[0]);
            MatchActionImportItem importItem = importItems.get(indices[1]);

            for (int i = 0; i < importItem.getNr(); i++) {
                TeamMatchAction teamMatchAction = new TeamMatchAction();
                teamMatchAction.setActionTypeId(ActionType.nameIdMap.get(importItem.getActionType()));
                teamMatchAction.setTeamId(teamMap.get(importItem.getTeam()).getId());

                matchActionMap.put(new TeamMatchActionKey(teamMatch.getTeamMatchKey(), actionId++), teamMatchAction);
            }

            int[] newIndices = getNextIndices(teamMatches, importItems, indices[0], indices[1] + 1, comparator);
            if (newIndices[0] > -1 && newIndices[0] != indices[0])
                actionId = tmam.getNewTeamMatchActionId(teamMatches.get(newIndices[0]).getTeamMatchKey());

            indices = newIndices;
        }
    }

    private int[] getNextIndices(List<? extends WithH2HMatchParticipantsDate> list1,
                                 List<? extends WithH2HMatchParticipantsDate> list2,
                                 int indX1, int indX2, Comparator<WithH2HMatchParticipantsDate> comparator) {
        while (indX1 < list1.size() && indX2 < list2.size() &&
                comparator.compare(list1.get(indX1), list2.get(indX2)) < 0)
            indX1++;

        while (indX1 < list1.size() && indX2 < list2.size() &&
                comparator.compare(list2.get(indX2), list1.get(indX1)) < 0)
            indX2++;

        return new int[]{indX1 < list1.size() ? indX1 : -1, indX2 < list2.size() ? indX2 : -1};
    }

    private static class MatchActionImportItem implements WithH2HMatchParticipantsDate {
        private String competition;
        private String teamHome;
        private String teamAway;
        private LocalDateTime date;
        private String team;
        private String actionType;
        private int nr;

        private Integer participant1Id;
        private Integer participant2Id;

        public String getCompetition() {
            return competition;
        }

        public void setCompetition(String competition) {
            this.competition = competition;
        }

        public String getTeamHome() {
            return teamHome;
        }

        public void setTeamHome(String teamHome) {
            this.teamHome = teamHome;
        }

        public String getTeamAway() {
            return teamAway;
        }

        public void setTeamAway(String teamAway) {
            this.teamAway = teamAway;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public String getTeam() {
            return team;
        }

        public void setTeam(String team) {
            this.team = team;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public int getNr() {
            return nr;
        }

        public void setNr(int nr) {
            this.nr = nr;
        }

        public Integer getParticipant1Id() {
            return participant1Id;
        }

        public void setParticipant1Id(int participant1Id) {
            this.participant1Id = participant1Id;
        }

        public Integer getParticipant2Id() {
            return participant2Id;
        }

        public void setParticipant2Id(int participant2Id) {
            this.participant2Id = participant2Id;
        }
    }
}
