package com.sportservlet.dispatch;

import com.sports.logic.calculation.DbCalculation;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareProcessManageH2HMatch extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        dispatchURL = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId)
                .getH2HObjectFactory().getProcessManagePath();
    }
}
