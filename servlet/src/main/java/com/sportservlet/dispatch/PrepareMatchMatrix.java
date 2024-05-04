package com.sportservlet.dispatch;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.logic.calculation.DbCalculation;
import com.sports.logic.factory.CompSeasonParticipantFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class PrepareMatchMatrix extends SuperDispatchServlet {
    @Override
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        CompSeasonParticipantFactory<? extends CompSeasonParticipantKey,
                ? extends CompSeasonPhaseParticipantKey,
                ? extends Participant,
                ? extends SuperKeyEntity,
                ? extends SuperKeyEntity,
                ? extends H2HMatchKey,
                ? extends H2HMatch,
                ? extends H2HMatchPartKey,
                ? extends H2HMatchPart,
                ? extends H2HMatchPartStatKey,
                ? extends H2HMatchPartStat> factory = new DbCalculation(stat).getCompSeasonParticipantFactory(competitionId);

        switch (factory.getParticipantType()) {
            case PERSON_SPORT -> dispatchURL = "MatchMatrixPersonSport";
            case DOUBLE -> dispatchURL = "MatchMatrixDouble";
            case TEAM -> dispatchURL = "MatchMatrixTeam";
        }
    }
}
