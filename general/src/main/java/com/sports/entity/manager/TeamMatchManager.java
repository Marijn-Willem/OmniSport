package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.TeamMatch;
import com.sports.entity.key.CompSeasonPhaseTeamKey;
import com.sports.entity.key.TeamMatchKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public List<TeamMatch> getPlayedMatchesCompSeasonPhaseTeam(List<CompSeasonPhaseTeamKey> keys)
        throws SQLException {
        return getPlayedMatches(keys, null);
    }

    public List<TeamMatch> getPlayedMatchesTeamAfterDate(List<CompSeasonPhaseTeamKey> compSeasonPhaseTeamKeys,
                                                         LocalDateTime startDate) throws SQLException {
        String dateString = QueryUtil.convertDateTimeToDbString(startDate);

        return getPlayedMatches(compSeasonPhaseTeamKeys, "\"date\" >= " + dateString);
    }

    public List<TeamMatch> getMatches(List<TeamMatchKey> teamMatchKeys) throws SQLException {
        return getEntityListFromSuperKeys(teamMatchKeys);
    }

    public List<TeamMatch> getMatchesByCompetitionsAndDates(List<Integer> competitionIds,
                                                            List<LocalDateTime> dateTimes) throws SQLException {
        List<TeamMatch> teamMatches = new ArrayList<>();

        if (!competitionIds.isEmpty() && !dateTimes.isEmpty()) {
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

    private List<TeamMatch> getPlayedMatches(String query) throws SQLException {
        List<TeamMatch> playedTeamMatches = new ArrayList<>();

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            playedTeamMatches.add(getInstanceFromResultSet(rs));

        return playedTeamMatches;
    }

    private List<TeamMatch> getPlayedMatches(List<CompSeasonPhaseTeamKey> keys, String whereClause)
            throws SQLException {
        List<TeamMatch> teamMatches = new ArrayList<>();

        if (!keys.isEmpty()) {
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

            teamMatches.addAll(getPlayedMatches(homeQuery + " UNION " + awayQuery));
        }

        return teamMatches;
    }
}
