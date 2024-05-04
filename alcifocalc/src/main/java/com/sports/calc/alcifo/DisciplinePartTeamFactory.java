package com.sports.calc.alcifo;

import com.sports.entity.*;
import com.sports.entity.key.*;
import com.sports.entity.manager.DisciplinePartTeamManager;
import com.sports.entity.manager.TeamManager;

import java.sql.Statement;

public class DisciplinePartTeamFactory extends AlcifoPartParticipantFactory<CompSeasonTeamKey,
        CompSeasonTeam,
        Team,
        EventTeamKey,
        EventTeam,
        DisciplinePartTeamKey,
        EventDisciplinePartKey,
        DisciplinePartTeam,
        EventPartTeamKey,
        EventPartTeam> {
    @Override
    public DisciplinePartTeamManager getManager(Statement stat) {
        return new DisciplinePartTeamManager(stat);
    }

    @Override
    public TeamManager getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public DisciplinePartTeamKey getKey(EventDisciplinePartKey partKey, int participantId) {
        EventPartTeamKey eventPartTeamKey = new EventPartTeamKey(partKey.getSuperKey(), participantId);
        return new DisciplinePartTeamKey(eventPartTeamKey, partKey.getEventDisciplinePartId());
    }

    @Override
    public CompSeasonEventPartKey getCompSeasonEventPartKey(EventDisciplinePartKey partKey) {
        return partKey.getSuperKey();
    }

    @Override
    public DisciplinePartTeam getInstance() {
        return new DisciplinePartTeam();
    }

    @Override
    public AlcifoParticipantFactory<CompSeasonTeamKey,
            CompSeasonTeam,
            Team,
            EventTeamKey,
            EventTeam,
            EventPartTeamKey,
            EventPartTeam> getParticipantFactory() {
        return new EventTeamFactory();
    }
}
