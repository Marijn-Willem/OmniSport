package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Club;
import com.sports.entity.ClubInstance;
import com.sports.entity.key.ClubInstanceKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ClubManager extends InstanceEntityManager<Club, ClubInstanceKey, ClubInstance> {
    public ClubManager(Statement stat) {
        super(stat);
    }

    String getTableName() {
        return "club";
    }

    String[] getValueColumns() {
        return new String[] {
                "name",
                "countrygeoid"
        };
    }

    Club getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Club club = new Club();

        club.setId(rs.getInt("id"));
        club.setName(rs.getString("name"));
        club.setCountryGeoId(QueryUtil.getIntegerFromResultSet(rs, "countrygeoid"));

        return club;
    }

    @Override
    EntityInstanceManager<ClubInstanceKey, ClubInstance> getEntityInstanceManager() {
        return new ClubInstanceManager(stat);
    }

    @Override
    ClubInstance getEntityInstance() {
        return new ClubInstance();
    }

    @Override
    ClubInstanceKey getEntityInstanceKey(ClubInstance entityInstance) {
        return new ClubInstanceKey(entityInstance.getEntityId(), entityInstance.getEntityInstanceId());
    }

    public Club getClubByName(String name) throws SQLException {
        return getEntityByName(name);
    }

    public List<Club> getClubsByNameLike(String name) throws SQLException {
        return getEntityListNameLike(name);
    }

    public List<Integer> getClubIdsFromCountryGeo(int countryGeoId) throws SQLException {
        return getIdList("countrygeoid = " + countryGeoId);
    }
}
