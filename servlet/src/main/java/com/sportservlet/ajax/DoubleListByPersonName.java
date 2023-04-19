package com.sportservlet.ajax;

import com.sports.entity.Double;
import com.sports.entity.Person;
import com.sports.entity.PersonSport;
import com.sports.entity.comparator.DescribedEntityDescription;
import com.sports.entity.manager.DoubleManager;
import com.sports.entity.manager.PersonManager;
import com.sports.entity.manager.PersonSportManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleListByPersonName extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String nm = req.getParameter("nm");
        Person person = new PersonManager(stat).getPersonByName(nm);

        if (person != null) {
            List<Integer> personSportIds = new PersonSportManager(stat).getPersonSportsForPerson(person.getId())
                    .stream().map(PersonSport::getId).collect(Collectors.toList());
            List<Double> doubles = new ArrayList<>(new DoubleManager(stat).getDoublesWithPersonSports(personSportIds).values());
            doubles.sort(new DescribedEntityDescription());

            Writer w = resp.getWriter();

            for (Double dbl : doubles)
                ServletUtil.writeOption(dbl.getId(), dbl.getDescription(), w);
        }
    }
}
