package com.management.servlet.dispatch;

import com.sports.entity.Club;
import com.sports.entity.manager.ClubManager;
import com.sportservlet.dispatch.SuperDispatchServlet;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class PrepareEntityInstancePortalClub extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        String nm= req.getParameter("nm");

        Club club = new ClubManager(stat).getClubByName(nm);

        if (club != null) {
            req.setAttribute("eid", club.getId());
            dispatchURL = "EntityInstancePortal";
        }
        else
            dispatchURL = "ClubPortal";
    }
}
