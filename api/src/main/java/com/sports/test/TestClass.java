package com.sports.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestClass {
    private static Statement statQ;
    private static Statement statU;

    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();

        String connStr = "jdbc:postgresql://localhost:5432/omnisport";
        Connection conn = DriverManager.getConnection(connStr, "postgres", "postgres");
        statQ = conn.createStatement();
        statU = conn.createStatement();

        conn.close();
        DriverManager.deregisterDriver(DriverManager.getDriver(connStr));
    }
}
