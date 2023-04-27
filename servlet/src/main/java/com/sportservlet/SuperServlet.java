package com.sportservlet;

import com.sports.db.execute.DatabaseExecutor;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.logic.async.ThreadUtil;
import com.sports.logic.util.Util;
import com.sportservlet.flush.CacheFlusher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;

public abstract class SuperServlet extends HttpServlet {
    protected Integer competitionId;
    protected Integer seasonId;
    protected CompSeasonKey compSeasonKey;
    protected String compSeasonUrlParameters;
    protected CacheFlusher cacheFlusher;

    protected abstract void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCommonParameters(req);

        new RequestHandler(req, resp).execute();

        if (cacheFlusher != null)
            ThreadUtil.executeAsync(Collections.singletonList(cacheFlusher), false, 1);
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

    private void setCommonParameters(HttpServletRequest req) {
        competitionId = Util.convertStringToInteger(req.getParameter("cid"));
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
            processBody(stat, req, resp);
        }
    }
}
