package com.sportservlet.html;

import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartTeamFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageDisciplinePartTeams extends ManageAlcifoPartParticipants {
    @Override
    EventDisciplinePartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(stat, req), edpid);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new DisciplinePartTeamFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("disciplinepartteam");
    }

    @Override
    void writeSpecificScriptTagVars(HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("edpid", getIntValuedParameterValue(req, "edpid"), w);
    }
}
