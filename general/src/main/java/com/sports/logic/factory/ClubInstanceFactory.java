package com.sports.logic.factory;

import com.sports.entity.ClubInstance;
import com.sports.entity.Team;
import com.sports.entity.key.ClubInstanceKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.ClubInstanceManager;
import com.sports.entity.manager.CompSeasonTeamManager;
import com.sports.entity.manager.EntityInstanceManager;
import com.sports.entity.manager.TeamManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClubInstanceFactory extends EntityInstanceFactory<ClubInstanceKey, ClubInstance> {
    @Override
    public EntityInstanceManager<ClubInstanceKey, ClubInstance> getManager(Statement stat) {
        return new ClubInstanceManager(stat);
    }

    @Override
    public ClubInstanceKey getKey(int entityId, int instanceId) {
        return new ClubInstanceKey(entityId, instanceId);
    }

    @Override
    public ClubInstance getInstance() {
        return new ClubInstance();
    }

    @Override
    public String getManagePath() {
        return "ManageClubInstance";
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManageClubInstance";
    }

    @Override
    public List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException {
        List<Team> teamList = new TeamManager(stat).getTeamListForClub(entityId);
        List<Integer> teamIds = teamList.stream().map(Team::getId).collect(Collectors.toList());
        return new ArrayList<>(new CompSeasonTeamManager(stat).getCompSeasonsForParticipants(teamIds));
    }
}
