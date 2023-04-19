package com.sportservlet.ajax;

import com.sports.entity.Person;
import com.sports.entity.manager.PersonManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PersonSearchList extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        String name = req.getParameter("nm");

        List<Person> PersonList = new PersonManager(stat).getPersonListNameLike(name);

        Writer w = res.getWriter();

        for (Person Person : PersonList) {
            String line = "<tr onclick=\"handleSelectPersonName(this);\"><td>" + Person.getName() + "</td></tr>\n";
            w.append(line);
        }
    }
}
