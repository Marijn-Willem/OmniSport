package com.sportservlet.ajax;

import com.sports.entity.Person;
import com.sports.entity.manager.PersonManager;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class SplitPerson extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String nm = req.getParameter("nm");
        Person person = new PersonManager(stat).getPersonByName(nm);

        String response;

        if (person != null) {
            new DbCalculation(stat).splitPerson(person);
            response = "Person successfully split";
        }
        else
            response = "Name does not exist";

        resp.getWriter().append(response);
    }
}
