package com.sports.test;

import com.sports.db.manager.DatabaseManager;
import com.sports.db.util.QueryUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestClass {
    private static Statement statQ;
    private static Statement statU;

    public static void main(String[] args) throws Exception {
        String dbName = "localhost:5432/omnisport";
        DatabaseManager.setProperties(dbName, "postgres", "postgres");

        Class.forName(QueryUtil.getDriverName()).getDeclaredConstructor().newInstance();

        Connection conn = DatabaseManager.getConnection();
        statQ = conn.createStatement();
        statU = conn.createStatement();

        try {
            migrateSportId(1, -1);
            migrateSportId(2, -2);
            migrateSportId(3, -3);
            migrateSportId(5, -4);
            migrateSportId(6, -5);
            migrateSportId(9, -6);
            migrateSportId(10, -7);
            migrateSportId(11, -8);
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        conn.close();
        DriverManager.deregisterDriver(DriverManager.getDriver(QueryUtil.getJdbcPrefix() + "://" + dbName.split("/")[0] + "/"));
    }

    private static void migrateSportId(int oldSportId, int newSportId) throws SQLException {
        insertNewId(oldSportId, newSportId, "sport", "id", "name, isteam, ish2h, hasmatchparts");
        insertNewId(oldSportId, newSportId, "sportdiscipline", "sportid", "sportdisciplineid, name, resulttypeid, resulttypeprecisionid");
        insertNewId(oldSportId, newSportId, "disciplinepart", "sportid", "sportdisciplineid, disciplinepartid, name, \"order\"");
        insertNewId(oldSportId, newSportId, "sportevent", "sportid", "sporteventid, name, pointssortasc, isteam");
        insertNewId(oldSportId, newSportId, "sporteventpart", "sportid", "sporteventid, sporteventpartid, sportdisciplineid, name, \"order\", weight");
        updateId(oldSportId, newSportId, "compseasonevent", "sportid");
        updateId(oldSportId, newSportId, "compseasoneventpart", "sportid");
        updateId(oldSportId, newSportId, "eventdisciplinepart", "sportid");
        updateId(oldSportId, newSportId, "competition", "sportid");
        updateId(oldSportId, newSportId, "team", "sportid");
        updateId(oldSportId, newSportId, "personsport", "sportid");
        deleteId(oldSportId, "sporteventpart", "sportid");
        deleteId(oldSportId, "sportevent", "sportid");
        deleteId(oldSportId, "disciplinepart", "sportid");
        deleteId(oldSportId, "sportdiscipline", "sportid");
        deleteId(oldSportId, "sport", "id");
    }

    private static void insertNewId(int oldId, int newId, String tableName, String idColumn, String columns) throws SQLException {
        String dbCurrentTime = QueryUtil.getDbCurrentTime();

        statU.execute("INSERT INTO " + tableName + " (" + idColumn + ", " + columns + ", created, modified) " +
                "SELECT " + newId + ", " + columns + ", " + dbCurrentTime + ", " + dbCurrentTime +
                "FROM " + tableName + " WHERE " + idColumn + " = " + oldId);
    }

    private static void updateId(int oldId, int newId, String tableName, String idColumn) throws SQLException {
        statU.execute("UPDATE " + tableName + " SET " + idColumn + " = " + newId + ", modified = " + QueryUtil.getDbCurrentTime() +
                " WHERE " + idColumn + " = " + oldId);
    }

    private static void deleteId(int oldId, String tableName, String idColumn) throws SQLException {
        statU.execute("DELETE FROM " + tableName + " WHERE " + idColumn + " = " + oldId);
    }
}
