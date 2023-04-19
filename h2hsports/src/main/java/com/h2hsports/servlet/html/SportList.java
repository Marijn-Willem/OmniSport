package com.h2hsports.servlet.html;

import com.sports.entity.Sport;
import com.sports.entity.manager.SportManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportList extends SuperHtmlServlet {
    public void initSpecificProperties(HttpServletRequest req) {

    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<Sport> sportList = new SportManager(stat).getH2HSportList();

        Writer w = res.getWriter();

        w.append("<div>\n");

        String line;

        for (Sport sport : sportList) {
            line = "<a href=\"" + path + "/CompSeasonPortal?spid=" + sport.getId() + "\">" +
                    sport.getName() + "</a><br/>\n";

            w.append(line);
        }

        line = "<br/>\n<a href=\"" + path + "/PersonSearch\">Manage persons</a>\n";
        w.append(line);

        w.append("</div>\n");
    }
}
