package com.sportservlet.ajax;

import com.sports.entity.Competition;
import com.sports.entity.Sport;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.manager.CompetitionManager;
import com.sports.entity.manager.SportManager;
import com.sports.logic.util.Util;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompetitionListFull extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<Sport> sports = new SportManager(stat).getFullSportList();
        List<Competition> competitions = new CompetitionManager(stat).getCompetitionList();

        competitions.sort(new AliasableName());

        for (Competition competition : competitions)
            ServletUtil.writeOption(competition.getId(), getOutputString(competition, sports), resp.getWriter());
    }

    private String getOutputString(Competition competition, List<Sport> sports) {
        String sportName = "";

        for (Sport sport : sports)
            if (sport.getId() == competition.getSportId()) {
                sportName = sport.getName();
                break;
            }

        return Util.concatStringsWithDelimiter(competition.getName(), sportName, " - ");
    }
}
