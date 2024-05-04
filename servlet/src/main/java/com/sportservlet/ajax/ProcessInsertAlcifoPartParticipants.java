package com.sportservlet.ajax;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DbCalculation;
import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.AlcifoParticipant;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.AlcifoParticipantKey;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.SuperKey;
import com.sportservlet.SuperResponseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class ProcessInsertAlcifoPartParticipants<APPK extends SuperKey, PK extends SuperKey, APP extends AlcifoPartParticipant> extends SuperResponseServlet {
    PK partKey;

    abstract PK getPartKey(HttpServletRequest req);
    abstract AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
            ? extends SuperKeyEntity,
            ? extends Participant,
            ? extends AlcifoParticipantKey,
            ? extends AlcifoParticipant,
            APPK,
            PK,
            APP,
            ? extends SuperKey,
            ? extends AlcifoPartParticipant> getFactory();
    boolean specificCheckBeforeInsert(Statement stat) throws SQLException { return true; }
    String getOutputSpecificCheckFail() { return null; }

    @Override
    protected void processBody(Statement stat, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException {
        partKey = getPartKey(req);
        AlcifoPartParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends SuperKeyEntity,
                ? extends Participant,
                ? extends AlcifoParticipantKey,
                ? extends AlcifoParticipant,
                APPK,
                PK,
                APP,
                ? extends SuperKey,
                ? extends AlcifoPartParticipant> factory = getFactory();
        List<APP> participants = factory.getManager(stat).getPartParticipantList(partKey);

        String output;

        if (!participants.isEmpty()) {
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
