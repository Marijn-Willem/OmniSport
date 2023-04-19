package com.sportservlet.ajax;

import com.sports.entity.Competition;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.manager.CompetitionManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompetitionList extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int sportId = Integer.parseInt(req.getParameter("spid"));

        List<Competition> competitions = new CompetitionManager(stat).getCompetitionList(sportId);

        competitions.sort(new AliasableName());

        for (Competition competition : competitions)
            ServletUtil.writeGenderAliasableOption(competition.getId(), competition, resp.getWriter());
    }
}
