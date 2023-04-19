package com.sports.db.manager;

import com.sports.db.util.QueryUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static Properties properties;
    private static String dbName;

    public static void setProperties(String dbName, String dbUser, String dbPwd) {
        properties = new Properties();
        properties.setProperty("user", dbUser);
        properties.setProperty("password", dbPwd);

        DatabaseManager.dbName = dbName;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(QueryUtil.getJdbcPrefix() + "://" + dbName, properties);
    }
}
