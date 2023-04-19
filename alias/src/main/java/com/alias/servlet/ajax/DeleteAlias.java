package com.alias.servlet.ajax;

import com.sports.entity.key.AliasKey;
import com.sports.entity.manager.AliasManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteAlias extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int aeid = getIntValuedParameterValue(req, "aeid");
        int aid = getIntValuedParameterValue(req, "aid");

        new AliasManager(stat).deleteAlias(new AliasKey(aeid, aid));

        resp.getWriter().append("Alias successfully removed");
    }
}
