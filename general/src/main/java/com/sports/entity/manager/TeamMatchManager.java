package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.TeamMatch;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TeamMatchManager extends H2HMatchManager<TeamMatchKey, TeamMatch> {
    public TeamMatchManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "teammatch";
    }

    public String getIdColumn() {
        return "teammatchid";
    }

    public String[] getSpecificValueColumns() {
        return new String[] {
                "scoreshootouthome",
                "scoreshootoutaway"
            };
    }

    protected String getParticipant1IdColumn() {
        return "teamhomeid";
    }

    protected String getParticipant2IdColumn() {
        return "teamawayid";
    }

    protected String getScore1Column() {
        return "scorehome";
    }

    protected String getScore2Column() {
        return "scoreaway";
    }

    @Override
    protected String getParticipant1NcrIdColumn() {
        return "teamhomencrid";
    }

    @Override
    protected String getParticipant2NcrIdColumn() {
        return "teamawayncrid";
    }

    protected TeamMatch getInstanceFromResultSet(ResultSet rs) throws SQLException {
        TeamMatch teamMatch = new TeamMatch();

        fillGenericPropertiesFromResultSet(teamMatch, rs);
        teamMatch.setCompetitionId(rs.getInt("competitionid"));
        teamMatch.setSeasonId(rs.getInt("seasonid"));
        teamMatch.setTeamMatchId(rs.getInt("teammatchid"));
        teamMatch.setScoreShootoutHome(QueryUtil.getIntegerFromResultSet(rs, "scoreshootouthome"));
        teamMatch.setScoreShootoutAway(QueryUtil.getIntegerFromResultSet(rs, "scoreshootoutaway"));

        return teamMatch;
    }

    @Override
    TeamMatchKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new TeamMatchKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    public void deleteMatches(List<TeamMatchKey> teamMatchKeys) throws SQLException {
        if (teamMatchKeys.size() > 0) {
            String query = "DELETE FROM teammatch WHERE (" + getConditionsKeyList(teamMatchKeys) + ")";
            stat.execute(query);
        }
    }

    public List<TeamMatch> getPlayedMatchesInCompSeason(CompSeasonKey csk) throws SQLException {
        return getPlayedMatches(getPlayedMatchQuery(csk.getWhereClause()));
    }

    public List<TeamMatch> getPlayedMatchesCompSeasonPhaseTeam(List<CompSeasonPhaseTeamKey> keys)
        throws SQLException {
        return getPlayedMatches(keys, null);
    }

    public List<TeamMatch> getPlayedMatchesTeamAfterDate(List<CompSeasonPhaseTeamKey> compSeasonPhaseTeamKeys,
                                                         LocalDateTime startDate) throws SQLException {
        String dateString = QueryUtil.convertDateTimeToDbString(startDate);

        return getPlayedMatches(compSeasonPhaseTeamKeys, "\"date\" >= " + dateString);
    }

    public List<TeamMatch> getMatchesWithDateInCompSeason(CompSeasonKey csk)
        throws SQLException {
        List<TeamMatch> teamMatchList = new ArrayList<TeamMatch>();

        String query = "SELECT " + getSelectColumnString() + " FROM teammatch " +
                "WHERE " + csk.getWhereClause() + " AND \"date\" IS NOT NULL";

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            teamMatchList.add(getInstanceFromResultSet(rs));

        return teamMatchList;
    }

    public List<TeamMatch> getMatches(List<TeamMatchKey> teamMatchKeys) throws SQLException {
        List<TeamMatch> teamMatchList = new ArrayList<TeamMatch>();

        if (teamMatchKeys.size() > 0) {
            String query = "SELECT " + getSelectColumnString() +
                    " FROM teammatch WHERE (" + getConditionsKeyList(teamMatchKeys) + ")";

            ResultSet rs = stat.executeQuery(query);

            while (rs.next())
                teamMatchList.add(getInstanceFromResultSet(rs));
        }

        return teamMatchList;
    }

    public Map<TeamMatchKey, TeamMatch> getMatchMap(List<TeamMatchKey> teamMatchKeys) throws SQLException {
        Map<TeamMatchKey, TeamMatch> matchMap = new HashMap<TeamMatchKey, TeamMatch>();

        List<TeamMatch> teamMatchList = getMatches(teamMatchKeys);

        for (TeamMatch teamMatch : teamMatchList)
            matchMap.put(teamMatch.getTeamMatchKey(), teamMatch);

        return matchMap;
    }

    public List<TeamMatchKey> getMatchKeysInPeriod(List<CompSeasonPhaseKey> compSeasonPhaseKeys,
                                                   LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<TeamMatchKey> teamMatchKeys = new ArrayList<TeamMatchKey>();

        if (compSeasonPhaseKeys.size() > 0) {
            String query = "SELECT " + getKeyColumnString() + " FROM teammatch " +
                    "WHERE (" + getConditionsKeyList(compSeasonPhaseKeys) + ") AND " +
                    "date BETWEEN " + QueryUtil.convertDateTimeToDbString(startDate) + " AND " +
                    QueryUtil.convertDateTimeToDbString(endDate);

            ResultSet rs = stat.executeQuery(query);

            while (rs.next()) {
                TeamMatchKey teamMatchKey = new TeamMatchKey(
                        new CompSeasonKey(rs.getInt("competitionid"), rs.getInt("seasonid")),
                        rs.getInt("teammatchid"));

                teamMatchKeys.add(teamMatchKey);
            }
        }

        return teamMatchKeys;
    }

    public List<TeamMatch> getEncounters(Collection<CompSeasonPhaseKey> compSeasonPhaseKeys,
                                         int team1Id, int team2Id, boolean strictOrder) throws SQLException {
        String whereClause = "(" + getConditionsKeyList(compSeasonPhaseKeys) + ") AND " +
                "((teamhomeid = " + team1Id + " AND teamawayid = " + team2Id + ")";

        whereClause = whereClause +
                (!strictOrder ? " OR (teamhomeid = " + team2Id + " AND teamawayid = " + team1Id + ")" : "") +
                ")";

        return getPlayedMatches(getPlayedMatchQuery(whereClause));
    }

    public List<TeamMatch> getMatchesByCompetitionsAndDates(List<Integer> competitionIds,
                                                            List<LocalDateTime> dateTimes) throws SQLException {
        List<TeamMatch> teamMatches = new ArrayList<>();

        if (competitionIds.size() > 0 && dateTimes.size() > 0) {
            String competitionIdsAsString = Util.concatStrings(
                    competitionIds.stream().map(x -> Integer.toString(x)).collect(Collectors.toList()),
                    ", ");
            String dateTimesAsString = Util.concatStrings(
                    dateTimes.stream().map(QueryUtil::convertDateTimeToDbString).collect(Collectors.toList()),
                    ", ");

            teamMatches = getEntityList("competitionid IN (" + competitionIdsAsString +
                    ") AND \"date\" IN (" + dateTimesAsString + ")");
        }

        return teamMatches;
    }

    public TeamMatchKey insertMatch(TeamMatch teamMatch) throws SQLException {
        CompSeasonKey csk = new CompSeasonKey(teamMatch.getCompetitionId(), teamMatch.getSeasonId());
        int matchId = getNewInt("teammatchid", csk.getWhereClause());

        TeamMatchKey teamMatchKey = new TeamMatchKey(csk, matchId);
        insert(teamMatchKey, teamMatch);

        return teamMatchKey;
    }

    public TeamMatchKey getNewMatchKey(CompSeasonKey csk) throws SQLException {
        int matchId = getNewInt("teammatchid", csk.getWhereClause());

        return new TeamMatchKey(csk, matchId);
    }

    public void insertMatches(Map<TeamMatchKey, TeamMatch> matchMap) throws SQLException {
        insert(matchMap);
    }

    private List<TeamMatch> getPlayedMatches(String query) throws SQLException {
        List<TeamMatch> playedTeamMatches = new ArrayList<TeamMatch>();

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            playedTeamMatches.add(getInstanceFromResultSet(rs));

        return playedTeamMatches;
    }

    private List<TeamMatch> getPlayedMatches(List<CompSeasonPhaseTeamKey> keys, String whereClause)
            throws SQLException {
        List<TeamMatch> teamMatches = new ArrayList<TeamMatch>();

        if (keys.size() > 0) {
            String[] clausesHome = new String[keys.size()];
            String[] clausesAway = new String[keys.size()];

            for (int i = 0; i < keys.size(); i++) {
                CompSeasonPhaseTeamKey cspck = keys.get(i);
                clausesHome[i] = "(" + cspck.getSuperKey().getWhereClause() + " AND teamhomeid = " +
                        cspck.getTeamId() + ")";
                clausesAway[i] = "(" + cspck.getSuperKey().getWhereClause() + " AND teamawayid = " +
                        cspck.getTeamId() + ")";
            }

            String suppl = (whereClause != null ? " AND " + whereClause : "");

            String homeQuery = getPlayedMatchQuery("(" + Util.concatStrings(clausesHome, " OR ") +
                    ")" + suppl);
            String awayQuery = getPlayedMatchQuery("(" + Util.concatStrings(clausesAway, " OR ") +
                    ")" + suppl);

            teamMatches = getPlayedMatches(homeQuery + " UNION " + awayQuery);
        }

        return teamMatches;
    }
}
