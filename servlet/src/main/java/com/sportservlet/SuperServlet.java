package com.sportservlet;

import com.sports.cache.key.CacheKey;
import com.sports.db.execute.DatabaseExecutor;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.async.ThreadUtil;
import com.sports.logic.async.ThreadWorker;
import com.sports.logic.util.Util;
import com.sports.web.util.ApiUtil;
import com.sportservlet.flush.CacheFlusher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public abstract class SuperServlet extends HttpServlet {
    protected Integer competitionId;
    protected Integer seasonId;
    protected CompSeasonKey compSeasonKey;
    protected String compSeasonUrlParameters;
    protected CacheFlusher cacheFlusher;

    protected abstract void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException;

    protected void init(Statement stat, HttpServletRequest req) throws SQLException { }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMethod(req, resp);
    }

    protected void doMethod(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCommonParameters(req);

        new RequestHandler(req, resp).execute();

        if (cacheFlusher != null)
            ThreadUtil.executeAsync(Collections.singletonList(new CacheFlushHandler()), false, 1);
    }

    protected int getIntValuedParameterValue(HttpServletRequest req, String parameter) {
        return Integer.parseInt(req.getParameter(parameter));
    }

    protected CompSeasonEventKey getCompSeasonEventKey(HttpServletRequest req) {
        int cseid = getIntValuedParameterValue(req, "cseid");

        return new CompSeasonEventKey(compSeasonKey, cseid);
    }

    protected CompSeasonEventPartKey getCompSeasonEventPartKey(HttpServletRequest req) {
        int csepid = getIntValuedParameterValue(req, "csepid");

        return new CompSeasonEventPartKey(getCompSeasonEventKey(req), csepid);
    }

    protected String getBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = req.getReader();
        String line;

        while ((line = br.readLine()) != null)
            sb.append(line);

        return sb.toString();
    }

    protected void applyCacheDelete(CacheKey cacheKey) throws URISyntaxException, IOException {
        URL url = new URI(ApiUtil.getCacheDeleteUrl(cacheKey)).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.getResponseMessage();
        connection.disconnect();
    }

    private void setCommonParameters(HttpServletRequest req) {
        competitionId = Util.convertStringToNegativeInteger(req.getParameter("cid"));
        seasonId = Util.convertStringToInteger(req.getParameter("sid"));

        if (competitionId != null && seasonId != null) {
            compSeasonKey = new CompSeasonKey(competitionId, seasonId);
            compSeasonUrlParameters = "cid=" + competitionId + "&sid=" + seasonId;
        } else {
            compSeasonKey = null;
            compSeasonUrlParameters = null;
        }
    }

    private class RequestHandler extends DatabaseExecutor {
        private final HttpServletRequest req;
        private final HttpServletResponse resp;

        public RequestHandler(HttpServletRequest req, HttpServletResponse resp) {
            this.req = req;
            this.resp = resp;
        }

        @Override
        public void doWork(Statement stat) throws Exception {
            init(stat, req);
            processBody(stat, req, resp);
        }
    }

    private class CacheFlushHandler implements ThreadWorker {
        @Override
        public void doWork() throws Exception {
            cacheFlusher.execute();
            List<CacheKey> cacheKeys = cacheFlusher.getCacheKeys();

            for (CacheKey cacheKey : cacheKeys)
                applyCacheDelete(cacheKey);
        }
    }
}
