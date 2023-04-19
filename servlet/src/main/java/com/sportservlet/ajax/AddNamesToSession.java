package com.sportservlet.ajax;

import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Statement;

public class AddNamesToSession extends SuperResponseServlet {
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] names = req.getParameterValues("nm");
        req.getSession().setAttribute("names", names);

        resp.getWriter().append("Names added in memory");
    }
}
