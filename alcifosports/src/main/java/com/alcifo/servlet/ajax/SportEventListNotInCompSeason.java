package com.alcifo.servlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.SportEvent;
import com.sports.entity.comparator.AliasableName;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportEventListNotInCompSeason extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        List<SportEvent> sportEventList = new DbCalculation(stat).getSportEventsNotInCompSeason(compSeasonKey);
        sportEventList.sort(new AliasableName());

        for (SportEvent sportEvent : sportEventList)
            ServletUtil.writeGenderAliasableOption(sportEvent.getSportEventId(), sportEvent, resp.getWriter());
    }
}
