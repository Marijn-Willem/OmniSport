package com.teamsports.servlet.html;

import com.sports.entity.Sport;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.SportManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportOverview extends SuperHtmlServlet {
    void initSpecificProperties(HttpServletRequest req) {

    }

    protected void processHtmlBody(Statement stat, HttpServletRequest req, HttpServletResponse res)
            throws IOException, SQLException {
        SportManager sm = new SportManager(stat);
        List<Sport> sportList = sm.getTeamSportsList();
        sportList.sort(new NamedEntityName());

        Writer w = res.getWriter();

        for (Sport sport : sportList)
            writeLink("CompSeasonPortal?spid=" + sport.getId(), sport.getName(), w);

        w.append("<br/>\n");
        writeLink("ManageTeams", "Manage Teams", w);
    }
}
