package com.sports.calc.alcifo;

import com.sports.entity.AlcifoPartParticipant;
import com.sports.entity.Participant;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.SuperKey;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.ParticipantManager;

import java.sql.Statement;
import java.util.Map;

public interface AlcifoPartParticipantFactory {
    AlcifoPartParticipantManager<? extends SuperKey, ? extends SuperKey, ? extends AlcifoPartParticipant> getManager(Statement stat);
    ParticipantManager<? extends Participant> getParticipantManager(Statement stat);
    Map<? extends SuperKey, ? extends AlcifoPartParticipant> getEmptyMap();
    SuperKey getKey(SuperKey partKey, int participantId);
    CompSeasonEventPartKey getCompSeasonEventPartKey(SuperKey partKey);
    AlcifoPartParticipant getInstance();
    AlcifoParticipantFactory getParticipantFactory();
}
