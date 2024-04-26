package com.sports.logic.factory;

import com.sports.entity.EquipeInstance;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.EquipeInstanceKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.EquipeInstanceManager;
import com.sports.entity.manager.TeamManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EquipeInstanceFactory extends EntityInstanceFactory<EquipeInstanceKey, EquipeInstance> {
    @Override
    public EquipeInstanceManager getManager(Statement stat) {
        return new EquipeInstanceManager(stat);
    }

    @Override
    public EquipeInstanceKey getKey(int entityId, int instanceId) {
        return new EquipeInstanceKey(entityId, instanceId);
    }

    @Override
    public EquipeInstance getInstance() {
        return new EquipeInstance();
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManageEquipeInstance";
    }

    @Override
    public List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException {
        List<Integer> teamIds = new TeamManager(stat).getTeamListForEquipe(entityId).stream().map(Team::getId).toList();
        return new CompSeasonTeamManager(stat).getCompSeasonsForParticipants(teamIds).stream().toList();
    }
}
