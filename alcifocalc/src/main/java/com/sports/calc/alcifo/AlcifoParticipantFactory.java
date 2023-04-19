package com.sports.calc.alcifo;

import com.sports.entity.AlcifoParticipant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.AlcifoParticipantManager;
import com.sports.entity.manager.CompSeasonParticipantManager;

import java.sql.Statement;

public interface AlcifoParticipantFactory {
    AlcifoParticipantManager<? extends SuperKey, ? extends AlcifoParticipant> getManager(Statement stat);
    CompSeasonParticipantManager<? extends CompSeasonParticipantKey, ? extends SuperKeyEntity> getCompSeasonParticipantManager(Statement stat);
    SuperKey getAlcifoParticipantKey(CompSeasonEventKey cseKey, int participantId);
    AlcifoParticipant getAlcifoParticipant();
    AlcifoPartParticipantFactory getEventPartParticipantFactory();
    AlcifoPartParticipantFactory getDisciplinePartParticipantFactory();
}
