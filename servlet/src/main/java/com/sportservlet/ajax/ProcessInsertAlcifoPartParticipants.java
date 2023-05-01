package com.sportservlet.ajax;

import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sportservlet.SuperResponseServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class ProcessInsertAlcifoPartParticipants<U extends SuperKey> extends SuperResponseServlet {
    U partKey;

    abstract U getPartKey(HttpServletRequest req);
    abstract AlcifoPartParticipantFactory getFactory();
    boolean specificCheckBeforeInsert(Statement stat) throws SQLException { return true; }
    String getOutputSpecificCheckFail() { return null; }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        partKey = getPartKey(req);
        AlcifoPartParticipantFactory factory = getFactory();
        List<? extends AlcifoPartParticipant> participants = ((AlcifoPartParticipantManager<?, U, ?>)factory.getManager(stat))
                .getPartParticipantList(partKey);

        String output;

        if (participants.size() == 0) {
            if (specificCheckBeforeInsert(stat)) {
                new DbCalculation(stat).insertPartParticipants(partKey, factory);
                output = "Participants successfully inserted";
            }
            else
                output = getOutputSpecificCheckFail();
        }
        else
            output = "Already participants present";

        resp.getWriter().append(output);
    }
}
