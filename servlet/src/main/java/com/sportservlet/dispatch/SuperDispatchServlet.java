package com.sportservlet.dispatch;

import com.sportservlet.SuperServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SuperDispatchServlet extends SuperServlet {
    protected String dispatchURL;

    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        process(stat, req);
    }

    protected abstract void process(Statement stat, HttpServletRequest req) throws SQLException;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        req.getRequestDispatcher(dispatchURL).forward(req, resp);
    }
}
