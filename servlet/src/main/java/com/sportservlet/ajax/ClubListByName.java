package com.sportservlet.ajax;

import com.sports.entity.Club;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.ClubManager;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ClubListByName extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        String nm = req.getParameter("nm");

        List<Club> clubList = new ClubManager(stat).getClubsByNameLike(nm);
        clubList.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (Club club : clubList) {
            w.append("<tr onclick=\"handleClickClubName(this);\"><td>");
            w.append(club.getName());
            w.append("</td></tr>\n");
        }
    }
}
