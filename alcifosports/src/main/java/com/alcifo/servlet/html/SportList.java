package com.alcifo.servlet.html;

import com.sports.entity.Sport;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.manager.SportManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportList extends SuperHtmlServlet {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {

    }

    @Override
    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        List<Sport> sportList = new SportManager(stat).getAlcifoSportList();
        sportList.sort(new AliasableName());

        Writer w = res.getWriter();

        w.append("<div>\n");

        for (Sport sport : sportList) {
            String line = "<a href=\"" + path + "/CompSeasonPortal?spid=" + sport.getId() + "\">" +
                    sport.getName() + "</a><br/>\n";

            w.append(line);
        }

        w.append("</div>\n");
    }
}
