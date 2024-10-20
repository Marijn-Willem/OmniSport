package com.sports.logic.factory;

import com.sports.entity.NocInstance;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.NocInstanceKey;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.EntityInstanceManager;
import com.sports.entity.manager.NocInstanceManager;
import com.sports.entity.manager.TeamManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NocInstanceFactory extends EntityInstanceFactory<NocInstanceKey, NocInstance> {
    @Override
    public EntityInstanceManager<NocInstanceKey, NocInstance> getManager(Statement stat) {
        return new NocInstanceManager(stat);
    }

    @Override
    public NocInstanceKey getKey(int entityId, int instanceId) {
        return new NocInstanceKey(entityId, instanceId);
    }

    @Override
    public NocInstance getInstance() {
        return new NocInstance();
    }

    @Override
    public String getManagePath() {
        return "ManageNocInstance";
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManageNocInstance";
    }

    @Override
    public List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException {
        List<Team> teamList = new TeamManager(stat).getTeamListForNoc(entityId);
        List<Integer> teamIds = teamList.stream().map(Team::getId).collect(Collectors.toList());
        return new ArrayList<>(new CompSeasonTeamManager(stat).getCompSeasonsForParticipants(teamIds));
    }
}
