package com.sports.db.execute;

import com.sports.db.manager.DatabaseManager;

import java.sql.Connection;
import java.sql.Statement;

public abstract class DatabaseExecutor {
    public abstract void doWork(Statement stat) throws Exception;

    public void execute() {
        try {
            Connection conn = DatabaseManager.getConnection();
            Statement stat = conn.createStatement();

            doWork(stat);

            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
