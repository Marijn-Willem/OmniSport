package com.sports.web.init;

import com.sports.db.manager.DatabaseManager;
import com.sports.db.util.QueryUtil;
import com.sports.web.util.ApiUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.DriverManager;

public class ContextInit implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String apiUrl = System.getenv("OMNISPORT_APIURL");
        String apiClient = System.getenv("OMNISPORT_APICLIENT");
        String apiPw = System.getenv("OMNISPORT_APIPW");

        ApiUtil.setApiConfiguration(apiUrl, apiClient, apiPw);

        String dbUser = System.getenv( "OMNISPORT_DBUSER");
        String dbName = System.getenv( "OMNISPORT_DBCONN");
        String dbPwd = System.getenv( "OMNISPORT_DBPW");

        DatabaseManager.setProperties(dbName, dbUser, dbPwd);

        String dbType = System.getenv( "OMNISPORT_DBTYPE");

        if (dbType != null)
            QueryUtil.setDbType(dbType);

        try {
            Class.forName(QueryUtil.getDriverName()).getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        String dbHost = System.getenv("OMNISPORT_DBCONN").split("/")[0];

        try {
            DriverManager.deregisterDriver(DriverManager.getDriver(QueryUtil.getJdbcPrefix() + "://" + dbHost + "/"));
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
