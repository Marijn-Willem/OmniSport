package com.alias.servlet.dispatch;

import com.sports.entity.Person;
import com.sports.entity.manager.PersonManager;
import com.sportservlet.dispatch.SuperDispatchServlet;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class PreparePersonForEntityInstancePortal extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        String nm = req.getParameter("nm");

        Person person = new PersonManager(stat).getPersonByName(nm);

        if (person != null) {
            req.setAttribute("eid", person.getId());
            dispatchURL = "EntityInstancePortal";
        }
        else
            dispatchURL = "PersonPortal";
    }
}
