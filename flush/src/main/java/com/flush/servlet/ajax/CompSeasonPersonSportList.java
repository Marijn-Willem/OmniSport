package com.flush.servlet.ajax;

import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.manager.CompSeasonPersonSportManager;
import com.sports.entity.manager.PersonSportManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CompSeasonPersonSportList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        List<Integer> personSportIds = new CompSeasonPersonSportManager(stat).getParticipantIdsCompSeason(compSeasonKey);
        List<PersonSport> personSports = new PersonSportManager(stat).getParticipantList(personSportIds);

        personSports.sort(new DescribedEntityDescription());

        Writer w = resp.getWriter();

        for (PersonSport personSport : personSports)
            ServletUtil.writeOption(personSport.getId(), personSport.getDescription(), w);
    }
}
