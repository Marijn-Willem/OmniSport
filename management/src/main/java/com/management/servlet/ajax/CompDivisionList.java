package com.management.servlet.ajax;

import com.sports.entity.CompDivision;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.CompDivisionManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompDivisionList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<CompDivision> compDivisions = new CompDivisionManager(stat).getCompDivisions(competitionId);
        compDivisions.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (CompDivision compDivision : compDivisions)
            ServletUtil.writeOption(compDivision.getCompDivisionId(), compDivision.getName(), w);
    }
}
