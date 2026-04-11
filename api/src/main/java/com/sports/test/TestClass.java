package com.sports.test;

import com.sports.db.manager.DatabaseManager;
import com.sports.db.util.QueryUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
    private static Statement statQ;
    private static Statement statU;

    static void main() throws Exception {
        String dbName = "localhost:5432/omnisport";
        QueryUtil.setDbType("postgres");
        DatabaseManager.setProperties(dbName, "postgres", "postgres");

        Class.forName(QueryUtil.getDriverName()).getDeclaredConstructor().newInstance();

        Connection conn = DatabaseManager.getConnection();
        statQ = conn.createStatement();
        statU = conn.createStatement();

        markParentMatches();
        ResultSet rs = getPhasesWithParentMatches();

        Statement statQPhase = conn.createStatement();

        while (rs.next())
            processPhase(
                    statQPhase,
                    rs.getInt("competitionid"),
                    rs.getInt("seasonid"),
                    rs.getInt("compseasonphaseid")
            );

        conn.close();
        DriverManager.deregisterDriver(DriverManager.getDriver(QueryUtil.getJdbcPrefix() + "://" + dbName.split("/")[0] + "/"));
    }

    private static void markParentMatches() throws SQLException {
        statU.execute("UPDATE compseasonphase SET parentmatchtypeid = -1, modified = NOW() " +
                "WHERE competitionid = 7 AND seasonid <> 3 " +
                "AND phasetypeid IN (2, 3, 4, 108) " +
                "AND parentmatchtypeid IS NULL");

        statU.execute("UPDATE compseasonphase SET parentmatchtypeid = -1, modified = NOW() " +
                "WHERE competitionid IN (10, 11) " +
                "AND phasetypeid IN (1, 2) " +
                "AND parentmatchtypeid IS NULL");

        statU.execute("UPDATE compseasonphase SET parentmatchtypeid = -2, modified = NOW() " +
                "WHERE competitionid = 45 " +
                "AND phasetypeid IN (34, 35, 36, 37) " +
                "AND parentmatchtypeid IS NULL");
    }

    private static ResultSet getPhasesWithParentMatches() throws SQLException {
        return statQ.executeQuery("SELECT competitionid, seasonid, compseasonphaseid FROM compseasonphase " +
                "WHERE parentmatchtypeid IS NOT NULL");
    }

    private static void processPhase(Statement statQPhase, int competitionId, int seasonId, int phaseId) throws SQLException {
        boolean isBasketball = competitionId == 45;

        List<Integer> teamHomeIds = new ArrayList<>();

        String sortOrder = isBasketball ? "" : " DESC";

        ResultSet rs = statQPhase.executeQuery("SELECT teamhomeid, teamawayid, MIN(date) AS minDate FROM teammatch " +
                "WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId + " AND compseasonphaseid = " + phaseId +
                " GROUP BY teamhomeid, teamawayid ORDER BY minDate" + sortOrder);

        while (rs.next() && !teamHomeIds.contains(rs.getInt("teamawayid"))) {
            int teamHomeId = rs.getInt("teamhomeid");
            int teamAwayId = rs.getInt("teamawayid");

            insertParentMatch(competitionId, seasonId, phaseId, teamHomeId, teamAwayId);

            teamHomeIds.add(teamHomeId);
        }

        updateParentMatchIds(competitionId, seasonId, phaseId);
        if (isBasketball)
            updateParentMatchScoresBb(statQPhase, competitionId, seasonId, phaseId);
        else
            updateParentMatchScoresFbAndRb(competitionId, seasonId, phaseId);
    }

    private static void insertParentMatch(int competitionId, int seasonId, int phaseId,
                                          int teamHomeId, int teamAwayId) throws SQLException {
        statU.execute("INSERT INTO teammatch (competitionid, seasonid, teammatchid, compseasonphaseid, teamhomeid, teamawayid, finished, created, modified) " +
                "SELECT " + competitionId + ", " + seasonId + ", MAX(teammatchid) + 1, " + phaseId + ", " +
                teamHomeId + ", " + teamAwayId + ", TRUE, NOW(), NOW() " +
                "FROM teammatch WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId);
    }

    private static void updateParentMatchIds(int competitionId, int seasonId, int phaseId) throws SQLException {
        statU.execute("UPDATE teammatch tm SET parentmatchid = tmp.teammatchid, modified = NOW() FROM teammatch tmp " +
                "WHERE tm.competitionid = tmp.competitionid AND tm.seasonid = tmp.seasonid AND tm.compseasonphaseid = tmp.compseasonphaseid " +
                "AND tm.teamhomeid IN (tmp.teamhomeid, tmp.teamawayid) " +
                "AND tm.competitionid = " + competitionId + " AND tm.seasonid = " + seasonId + " AND tm.compseasonphaseid = " + phaseId +
                " AND tm.date IS NOT NULL AND tmp.date IS NULL");
    }

    private static void updateParentMatchScoresFbAndRb(int competitionId, int seasonId, int phaseId) throws SQLException {
        statU.execute("UPDATE teammatch tm SET scorehome = tm2.scorehome + tm1.scoreaway, " +
                "scoreaway = tm2.scoreaway + tm1.scorehome, " +
                "scoreshootouthome = tm2.scoreshootouthome, " +
                "scoreshootoutaway = tm2.scoreshootoutaway, " +
                "modified = NOW() FROM teammatch tm1, teammatch tm2 " +
                "WHERE tm.competitionid = tm1.competitionid AND tm.seasonid = tm1.seasonid AND tm.teammatchid = tm1.parentmatchid " +
                "AND tm.competitionid = tm2.competitionid AND tm.seasonid = tm2.seasonid AND tm.teammatchid = tm2.parentmatchid " +
                "AND tm1.date < tm2.date " +
                "AND tm.competitionid = " + competitionId + " AND tm.seasonid = " + seasonId + " AND tm.compseasonphaseid = " + phaseId);
    }

    private static void updateParentMatchScoresBb(Statement statQPhase, int competitionId, int seasonId, int phaseId)
            throws SQLException {
        statU.execute("UPDATE teammatch SET scorehome = 0, scoreaway = 0, modified = NOW() " +
                "WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId +
                " AND compseasonphaseid = " + phaseId + " AND parentmatchid IS NULL");

        ResultSet rs = statQPhase.executeQuery("SELECT teamhomeid, teamawayid, parentmatchid, scorehome, scoreaway " +
                "FROM teammatch WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId +
                " AND compseasonphaseid = " + phaseId + " AND parentmatchid IS NOT NULL");

        while (rs.next()) {
            int teamHomeId = rs.getInt("teamhomeid");
            int teamAwayId = rs.getInt("teamawayid");
            int parentMatchId = rs.getInt("parentmatchid");
            boolean homeWin = rs.getInt("scorehome") > rs.getInt("scoreaway");

            if (homeWin) {
                statU.execute("UPDATE teammatch SET scorehome = scorehome + 1, modified = NOW() " +
                        "WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId +
                        " AND teammatchid = " + parentMatchId + " AND teamhomeid = " + teamHomeId);

                statU.execute("UPDATE teammatch SET scoreaway = scoreaway + 1, modified = NOW() " +
                        "WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId +
                        " AND teammatchid = " + parentMatchId + " AND teamawayid = " + teamHomeId);
            }
            else {
                statU.execute("UPDATE teammatch SET scorehome = scorehome + 1, modified = NOW() " +
                        "WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId +
                        " AND teammatchid = " + parentMatchId + " AND teamhomeid = " + teamAwayId);

                statU.execute("UPDATE teammatch SET scoreaway = scoreaway + 1, modified = NOW() " +
                        "WHERE competitionid = " + competitionId + " AND seasonid = " + seasonId +
                        " AND teammatchid = " + parentMatchId + " AND teamawayid = " + teamAwayId);
            }
        }
    }
}
