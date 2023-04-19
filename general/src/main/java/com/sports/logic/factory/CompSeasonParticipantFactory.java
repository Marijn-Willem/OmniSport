package com.sports.logic.factory;

import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.CompSeasonParticipantManager;
import com.sports.entity.manager.CompSeasonPhaseParticipantManager;
import com.sports.entity.manager.ParticipantManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public interface CompSeasonParticipantFactory<S extends CompSeasonParticipantKey, T extends SuperKeyEntity> {
    H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> getH2HObjectFactory();
    CompSeasonParticipantManager<S, T> getCompSeasonParticipantManager(Statement stat);
    ParticipantManager<? extends Participant> getParticipantManager(Statement stat);
    CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey,
            ? extends CompSeasonPhaseParticipantKey, ? extends SuperKeyEntity> getPhaseParticManager(Statement stat);
    CompSeasonParticipantKey getCompSeasonParticKey(CompSeasonKey csk, int specifId);
    CompSeasonPhaseParticipantKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId);
    Map<String, ? extends Participant> getDescriptionParticipantMap(Statement stat, List<String> descriptions,
                                                                    int competitionId) throws SQLException;
    ParticipantType getParticipantType();
    String getParticipantDescription();
}
