package com.sports.calc.alcifo;

import com.sports.entity.DisciplinePartTeam;
import com.sports.entity.Team;
import com.sports.entity.key.*;
import com.sports.entity.manager.AlcifoPartParticipantManager;
import com.sports.entity.manager.DisciplinePartTeamManager;
import com.sports.entity.manager.ParticipantManager;
import com.sports.entity.manager.TeamManager;

import java.sql.Statement;

public class DisciplinePartTeamFactory implements AlcifoPartParticipantFactory {
    @Override
    public AlcifoPartParticipantManager<DisciplinePartTeamKey, EventDisciplinePartKey, DisciplinePartTeam> getManager(Statement stat) {
        return new DisciplinePartTeamManager(stat);
    }

    @Override
    public ParticipantManager<Team> getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public DisciplinePartTeamKey getKey(SuperKey partKey, int participantId) {
        EventDisciplinePartKey edpKey = (EventDisciplinePartKey)partKey;
        EventPartTeamKey eventPartTeamKey = new EventPartTeamKey(edpKey.getSuperKey(), participantId);
        return new DisciplinePartTeamKey(eventPartTeamKey, edpKey.getEventDisciplinePartId());
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(SuperKey partKey) {
        return ((EventDisciplinePartKey)partKey).getSuperKey();
    }

    @Override
    public DisciplinePartTeam getInstance() {
        return new DisciplinePartTeam();
    }

    @Override
    public AlcifoParticipantFactory getParticipantFactory() {
        return new EventTeamFactory();
    }
}
