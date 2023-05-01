package com.alcifo.servlet.dispatch;

import com.sportservlet.dispatch.SuperDispatchServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Statement;

public class AddCompSeasonEvent extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) {
        dispatchURL = "ManageCompSeasonEvent?" + compSeasonUrlParameters + "&md=i";
    }
}
