package com.sports.logic.factory;

import com.sports.entity.CompSeasonTeam;
import com.sports.entity.H2HMatch;
import com.sports.entity.Participant;
import com.sports.entity.SuperKeyEntity;
import com.sports.entity.key.*;
import com.sports.entity.manager.*;

import java.sql.Statement;

public class CompSeasonTeamFactory implements CompSeasonParticipantFactory<CompSeasonTeamKey, CompSeasonTeam> {
    @Override
    public H2HObjectFactory<? extends H2HMatchKey, ? extends H2HMatch> getH2HObjectFactory() {
        return new TeamMatchObjectFactory();
    }

    @Override
    public CompSeasonParticipantManager<CompSeasonTeamKey, CompSeasonTeam> getCompSeasonParticipantManager(Statement stat) {
        return new CompSeasonTeamManager(stat);
    }

    @Override
    public ParticipantManager<? extends Participant> getParticipantManager(Statement stat) {
        return new TeamManager(stat);
    }

    @Override
    public CompSeasonPhaseParticipantManager<? extends CompSeasonParticipantKey, ? extends CompSeasonPhaseParticipantKey, ? extends SuperKeyEntity> getPhaseParticManager(Statement stat) {
        return new CompSeasonPhaseTeamManager(stat);
    }

    @Override
    public CompSeasonParticipantKey getCompSeasonParticKey(CompSeasonKey csk, int specifId) {
        return new CompSeasonTeamKey(csk, specifId);
    }

    @Override
    public CompSeasonPhaseParticipantKey getPhaseParticKey(CompSeasonPhaseKey cspk, int specifId) {
        return new CompSeasonPhaseTeamKey(cspk, specifId);
    }

    @Override
    public ParticipantType getParticipantType() {
        return ParticipantType.TEAM;
    }

    @Override
    public String getParticipantDescription() {
        return "Team";
    }
}
