package com.teamsports.servlet.ajax;

import com.sports.entity.Sport;
import com.sports.entity.comparator.NamedEntityName;
import com.sports.entity.manager.SportManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        SportManager sm = new SportManager(stat);
        List<Sport> sportList = sm.getTeamSportsList();
        sportList.sort(new NamedEntityName());

        Writer w = resp.getWriter();

        for (Sport sport : sportList)
            ServletUtil.writeOption(sport.getId(), sport.getName(), w);
    }
}
