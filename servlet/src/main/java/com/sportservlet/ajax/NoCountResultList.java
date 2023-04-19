package com.sportservlet.ajax;

import com.sports.entity.NoCountResult;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.NoCountResultManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class NoCountResultList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<NoCountResult> noCountResults = new NoCountResultManager(stat).getNoCountResults();
        noCountResults.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (NoCountResult noCountResult : noCountResults)
            ServletUtil.writeOption(noCountResult.getId(), noCountResult.getName(), w);
    }
}
