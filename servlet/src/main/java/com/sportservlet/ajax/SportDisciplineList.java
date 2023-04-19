package com.sportservlet.ajax;

import com.sports.entity.SportDiscipline;
import com.sports.entity.comparator.AliasableName;
import com.sports.entity.manager.SportDisciplineManager;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportDisciplineList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        int spid = getIntValuedParameterValue(req, "spid");
        List<SportDiscipline> sportDisciplines = new SportDisciplineManager(stat).getSportDisciplinesForSport(spid);
        sportDisciplines.sort(new AliasableName());

        for (SportDiscipline sportDiscipline : sportDisciplines)
            ServletUtil.writeOption(sportDiscipline.getSportDisciplineId(), sportDiscipline.getName(), resp.getWriter());
    }
}
