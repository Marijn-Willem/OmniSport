package com.sports.test;

import com.sports.db.manager.DatabaseManager;
import com.sports.db.util.QueryUtil;
import com.sports.logic.util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestClass {
    private static Statement statQ;
    private static Statement statU;

    public static void main(String[] args) throws Exception {
        String dbName = "localhost:5432/omnisport";
        QueryUtil.setDbType("postgres");
        DatabaseManager.setProperties(dbName, "postgres", "postgres");

        Class.forName(QueryUtil.getDriverName()).getDeclaredConstructor().newInstance();

        Connection conn = DatabaseManager.getConnection();
        statQ = conn.createStatement();
        statU = conn.createStatement();

        conn.close();
        DriverManager.deregisterDriver(DriverManager.getDriver(QueryUtil.getJdbcPrefix() + "://" + dbName.split("/")[0] + "/"));
    }

    private static void insertNewId(int oldId, int newId, String tableName, String idColumn, String columns) throws SQLException {
        insertNewId(oldId, newId, tableName, idColumn, columns, null);
    }

    private static void insertNewId(int oldId, int newId, String tableName, String idColumn, String columns,
                                    String whereClauseAddition) throws SQLException {
        String dbCurrentTime = QueryUtil.getDbCurrentTime();

        statU.execute("INSERT INTO " + tableName + " (" + idColumn + ", " + columns + ", created, modified) " +
                "SELECT " + newId + ", " + columns + ", " + dbCurrentTime + ", " + dbCurrentTime +
                "FROM " + tableName + " WHERE " + getWhereClause(oldId, idColumn, whereClauseAddition));
    }

    private static void updateId(int oldId, int newId, String tableName, String idColumn) throws SQLException {
        updateId(oldId, newId, tableName, idColumn, null);
    }

    private static void updateId(int oldId, int newId, String tableName, String idColumn, String whereClauseAddition)
            throws SQLException {
        statU.execute("UPDATE " + tableName + " SET " + idColumn + " = " + newId + ", modified = " + QueryUtil.getDbCurrentTime() +
                " WHERE " + getWhereClause(oldId, idColumn, whereClauseAddition));
    }

    private static void deleteId(int oldId, String tableName, String idColumn) throws SQLException {
        deleteId(oldId, tableName, idColumn, null);
    }

    private static void deleteId(int oldId, String tableName, String idColumn, String whereClauseAddition) throws SQLException {
        statU.execute("DELETE FROM " + tableName + " WHERE " + getWhereClause(oldId, idColumn, whereClauseAddition));
    }

    private static String getWhereClause(int oldId, String idColumn, String whereClauseAddition) {
        return Util.concatStringsWithDelimiter(whereClauseAddition, idColumn + " = " + oldId, " AND ");
    }
}
