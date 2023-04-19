package com.sportservlet.ajax;

import com.sports.entity.Sport;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.manager.SportManager;
import com.sportservlet.SuperResponseServlet;

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
        Writer w = resp.getWriter();

        List<Sport> sportList = new SportManager(stat).getFullSportList();
        sportList.sort(new AliasableName());
        
        for (Sport sport : sportList) {
            boolean isAlcifo = !sport.isH2H() && !sport.isTeam();
            String optionText = "<option value=\"" + sport.getId() + "\"" + (isAlcifo ? " class=\"alcifo\"" : "") +
                    ">" + sport.getName() + "</option>\n";

            w.append(optionText);
        }
    }
}
