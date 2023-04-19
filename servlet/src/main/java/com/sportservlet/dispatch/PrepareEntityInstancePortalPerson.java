package com.sportservlet.dispatch;

import com.sports.entity.Person;
import com.sports.entity.manager.PersonManager;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareEntityInstancePortalPerson extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        Person person = new PersonManager(stat).getPersonByName(req.getParameter("nm"));

        if (person != null) {
            req.setAttribute("eid", person.getId());
            dispatchURL = "EntityInstancePortalPerson";
        }
        else
            dispatchURL = "PersonSearch";
    }
}
