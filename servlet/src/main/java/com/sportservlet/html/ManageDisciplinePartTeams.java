package com.sportservlet.html;

import com.sports.calc.alcifo.AlcifoPartParticipantFactory;
import com.sports.calc.alcifo.DisciplinePartTeamFactory;
import com.sports.entity.key.EventDisciplinePartKey;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Writer;

public abstract class ManageDisciplinePartTeams extends ManageAlcifoPartParticipants {
    @Override
    EventDisciplinePartKey getPartKey(HttpServletRequest req) {
        int edpid = getIntValuedParameterValue(req, "edpid");

        return new EventDisciplinePartKey(getCompSeasonEventPartKey(req), edpid);
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
