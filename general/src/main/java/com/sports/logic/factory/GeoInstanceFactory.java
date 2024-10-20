package com.sports.logic.factory;

import com.sports.entity.GeoInstance;
import com.sports.entity.Noc;
import com.sports.entity.Team;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.GeoInstanceKey;
import com.sports.entity.manager.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeoInstanceFactory extends EntityInstanceFactory<GeoInstanceKey, GeoInstance> {
    @Override
    public EntityInstanceManager<GeoInstanceKey, GeoInstance> getManager(Statement stat) {
        return new GeoInstanceManager(stat);
    }

    @Override
    public GeoInstanceKey getKey(int entityId, int instanceId) {
        return new GeoInstanceKey(entityId, instanceId);
    }

    @Override
    public GeoInstance getInstance() {
        return new GeoInstance();
    }

    @Override
    public String getManagePath() {
        return "ManageGeoInstance";
    }

    @Override
    public String getProcessManagePath() {
        return "ProcessManageGeoInstance";
    }

    @Override
    public List<CompSeasonKey> getCompSeasonsRelatedToEntity(int entityId, Statement stat) throws SQLException {
        List<Noc> nocList = new NocManager(stat).getNocsFromGeo(entityId);
        List<Integer> nocIds = nocList.stream().map(Noc::getId).collect(Collectors.toList());
        List<Integer> teamIds = new TeamManager(stat).getTeamListForNocs(nocIds).stream()
                .map(Team::getId).collect(Collectors.toList());

        return new ArrayList<>() {{
            addAll(new CompSeasonTeamManager(stat).getCompSeasonsForParticipants(teamIds));
            addAll(new EventPartLocationManager(stat).getEventPartLocationsForGeo(entityId)
                    .stream().map(x -> x.getSuperKey().getSuperKey().getSuperKey()).toList());
        }};
    }
}
