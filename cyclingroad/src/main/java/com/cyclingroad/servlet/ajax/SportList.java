package com.cyclingroad.servlet.ajax;

import com.sports.entity.Sport;
import com.sportservlet.SuperResponseServlet;
import com.sportservlet.util.ServletUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Statement;

public class SportList extends SuperResponseServlet {
    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletUtil.writeOption(Sport.sportIdCyclingRoad, Sport.getNameFromId(Sport.sportIdCyclingRoad), resp.getWriter());
    }
}
