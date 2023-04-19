package com.sports.logic.factory;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.H2HMatchManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.logic.calculation.StandingProcessor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public interface H2HObjectFactory<S extends H2HMatchKey, T extends H2HMatch> {
    H2HMatchManager<S, T> getManager(Statement stat);
    S getKey(CompSeasonKey compSeasonKey, int specifId);
    T getMatch();
    StandingProcessor getStandingProcessor(Statement stat, CompSeasonPhaseKey cspk) throws SQLException;
    H2HPartObjectFactory<? extends H2HMatchPartKey, ? extends H2HMatchPart>
        getPartObjectFactory();
    boolean showMatchListForPhase();
}
