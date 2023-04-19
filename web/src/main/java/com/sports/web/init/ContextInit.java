package com.sports.web.init;

import com.sports.cache.util.CacheUtil;
import com.sports.db.manager.DatabaseManager;
import com.sports.db.util.QueryUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.DriverManager;

public class ContextInit implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String dbUser = System.getenv( "OMNISPORT_DBUSER");
        String dbName = System.getenv( "OMNISPORT_DBCONN");
        String dbPwd = System.getenv( "OMNISPORT_DBPW");

        DatabaseManager.setProperties(dbName, dbUser, dbPwd);

        String dbType = System.getenv( "OMNISPORT_DBTYPE");

        if (dbType != null)
            QueryUtil.setDbType(dbType);

        String memHost = System.getenv( "OMNISPORT_MEMHOST");
        String memPort = System.getenv( "OMNISPORT_MEMPORT");
        String memGroupId = servletContextEvent.getServletContext().getInitParameter("MEM_GROUPID");

        try {
            CacheUtil.setConfiguration(memGroupId, memHost, Integer.parseInt(memPort));
            Class.forName(QueryUtil.getDriverName()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        CacheUtil.close();
        String dbHost = System.getenv("OMNISPORT_DBCONN").split("/")[0];

        try {
            DriverManager.deregisterDriver(DriverManager.getDriver(QueryUtil.getJdbcPrefix() + "://" + dbHost + "/"));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
