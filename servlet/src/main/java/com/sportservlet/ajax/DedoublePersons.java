package com.sportservlet.ajax;

import com.sports.entity.manager.PersonManager;
import com.sports.logic.calculation.DbCalculation;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;

public class DedoublePersons extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String p1 = req.getParameter("p1");
        String p2 = req.getParameter("p2");

        Map<String, Integer> personNameMap = new PersonManager(stat).getNamePersonIdMap(Arrays.asList(p1, p2));

        String output;

        if (personNameMap.containsKey(p1) && personNameMap.containsKey(p2)) {
            new DbCalculation(stat).dedoublePersons(personNameMap.get(p1), personNameMap.get(p2));
            output = p1 + " dedoubled to " + p2;
        }
        else
            output = "Non-existing person in " + p1 + ", " + p2;

        resp.getWriter().append(output);
    }
}
