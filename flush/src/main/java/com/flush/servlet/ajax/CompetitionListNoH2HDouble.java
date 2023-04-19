package com.flush.servlet.ajax;

import com.sports.entity.Competition;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.CompetitionManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompetitionListNoH2HDouble extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Competition> competitionList = new CompetitionManager(stat).getCompetitionListNoH2HDouble();
        competitionList.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (Competition competition : competitionList)
            ServletUtil.writeOption(competition.getId(), competition.getName(), w);
    }
}
