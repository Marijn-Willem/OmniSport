package com.flush.servlet.ajax;

import com.sports.cache.key.CacheFragmentKey;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Flush extends SuperResponseServlet {
    abstract CacheFragmentKey getCacheKey(Statement stat, HttpServletRequest req) throws SQLException;

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        String response = "Flush key could not be established";

        CacheFragmentKey cacheKey = getCacheKey(stat, req);

        if (cacheKey != null) {
            try {
                applyCacheDelete(cacheKey);
                response = "Flush successful";
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        resp.getWriter().append(response);
    }
}
