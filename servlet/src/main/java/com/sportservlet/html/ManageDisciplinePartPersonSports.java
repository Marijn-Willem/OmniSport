package com.sportservlet.html;

import com.sports.entity.key.EventDisciplinePartKey;
import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ManageDisciplinePartPersonSports extends ManageAlcifoPartParticipants {
    @Override
    EventDisciplinePartKey getPartKey(Statement stat, HttpServletRequest req) throws SQLException {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(stat, req), edpid);
    }

    @Override
    AlcifoPartParticipantFactory getFactory() {
        return new DisciplinePartPersonSportFactory();
    }

    @Override
    void initSpecificJsProperties() {
        jsList.add("disciplinepartpersonsport");
    }

    @Override
    void writeSpecificScriptTagVars(HttpServletRequest req, Writer w) throws IOException {
        writeVarInScriptTag("edpid", getIntValuedParameterValue(req, "edpid"), w);
    }
}
