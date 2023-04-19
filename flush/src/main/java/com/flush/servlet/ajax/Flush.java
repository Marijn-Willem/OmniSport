package com.flush.servlet.ajax;

import com.sports.cache.key.CacheKey;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Flush extends SuperResponseServlet {
    abstract CacheKey getCacheKey(Statement stat, HttpServletRequest req) throws SQLException;

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        String response;

        CacheKey cacheKey = getCacheKey(stat, req);

        if (cacheKey != null) {
            cacheKey.delete();
            response = "Flush successful";
        }
        else
            response = "Flush key could not be established";

        resp.getWriter().append(response);
    }
}
