package com.darts.servlet.dispatch;

import com.sports.entity.PersonMatch;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.manager.PersonMatchManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;

public class PrepareMatchStats extends SuperDispatchServlet {
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        int mid = getIntValuedParameterValue(req, "mid");
        PersonMatch personMatch = new PersonMatchManager(stat).getPersonMatch(new PersonMatchKey(compSeasonKey, mid));

        if (personMatch.getPersonSport1Id() != null && personMatch.getPersonSport2Id() != null) {
            prepareMatch(stat, req, personMatch);
            dispatchURL = "MatchStats";
        }
        else
            dispatchURL = "ManageDartsMatch";
    }
}
