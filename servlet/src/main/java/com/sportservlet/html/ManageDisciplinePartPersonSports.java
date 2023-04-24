package com.sportservlet.html;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartPersonSportFactory;
import com.sports.entity.key.EventDisciplinePartKey;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;

public abstract class ManageDisciplinePartPersonSports extends ManageAlcifoPartParticipants {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
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
