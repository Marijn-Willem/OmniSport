package com.alias.servlet.ajax;

import com.sports.entity.Club;
import com.sports.entity.manager.ClubManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class GetClubIdByName extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String nm = req.getParameter("nm");
        Club club = new ClubManager(stat).getClubByName(nm);

        String clubId = club != null ? Integer.toString(club.getId()) : "";

        resp.getWriter().append(clubId);
    }
}
