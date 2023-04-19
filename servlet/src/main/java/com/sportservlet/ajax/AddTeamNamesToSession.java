package com.sportservlet.ajax;

import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Statement;

public class AddTeamNamesToSession extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] names = req.getParameterValues("nm");

        req.getSession().setAttribute("teamNames", names);

        resp.getWriter().append("Names added to memory");
    }
}
