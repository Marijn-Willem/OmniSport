package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Team;
import com.sports.entity.key.TeamDescriptionSportIdGenderIdKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class TeamManager extends ParticipantManager<Team> {
    public TeamManager(Statement stat) {
        super(stat);
    }

    String getTableName() {
        return "team";
    }

    String[] getSpecificValueColumns() {
        return new String[] {
                "clubid",
                "nocid",
                "equipeid",
                "sportid",
                "genderid"
            };
    }

    @Override
    Team getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Team team = new Team();
        fillGeneralPropertiesFromResultSet(rs, team);

        team.setClubId(QueryUtil.getIntegerFromResultSet(rs, "clubid"));
        team.setNocId(QueryUtil.getIntegerFromResultSet(rs, "nocid"));
        team.setEquipeId(QueryUtil.getIntegerFromResultSet(rs, "equipeid"));
        team.setSportId(rs.getInt("sportid"));
        team.setGenderId(rs.getInt("genderid"));

        return team;
    }

    public Map<Integer, Team> getTeamMap(Collection<Integer> idColl) throws SQLException {
        return getParticipantMap(new ArrayList<>(idColl));
    }

    public List<Team> getTeamList(List<Integer> idList) throws SQLException {
        return getParticipantList(idList);
    }

    public Map<String, Team> getDescrTeamMapBySportGender(List<String> descriptions, int sportId, int genderId)
            throws SQLException {
        Map<String, Team> map = new HashMap<>();

        if (descriptions.size() > 0) {
            String whereClause = "description IN (" + getCommaSepStringList(descriptions) + ") " +
                    "AND sportid = " + sportId + " AND genderid = " + genderId;

            ResultSet rs = stat.executeQuery(getGenericQuery(whereClause));

            while (rs.next())
                map.put(rs.getString("description"), getInstanceFromResultSet(rs));
        }

        return map;
    }

    public Map<TeamDescriptionSportIdGenderIdKey, Integer> getTeamMapFromDescrSportGender(
            List<TeamDescriptionSportIdGenderIdKey> keys) throws SQLException {
        Map<TeamDescriptionSportIdGenderIdKey, Integer> teamMap = new HashMap<TeamDescriptionSportIdGenderIdKey, Integer>();

        ResultSet rs = stat.executeQuery(getGenericQuery(getConditionsKeyList(keys)));

        while (rs.next()) {
            TeamDescriptionSportIdGenderIdKey key = new TeamDescriptionSportIdGenderIdKey(
                    rs.getString("description"),
                    rs.getInt("sportid"),
                    rs.getInt("genderid")
            );

            teamMap.put(key, rs.getInt("id"));
        }

        return teamMap;
    }

    public List<Team> getTeamListForSport(int sportId) throws SQLException {
        return getEntityList("sportid = " + sportId);
    }

    public List<Team> getTeamListForSportGender(int sportId, int genderId) throws SQLException {
        return getEntityList("sportid = " + sportId + " AND genderid = " + genderId);
    }

    public List<Team> getTeamListForSportGenderClubs(int sportId, int genderId, List<Integer> clubIds) throws SQLException {
        List<Team> teamList = new ArrayList<Team>();

        if (clubIds.size() > 0)
            teamList =  getEntityList("sportid = " + sportId + " AND genderid = " + genderId + " AND clubid IN (" +
                getCommaSepIntList(clubIds) + ")");

        return teamList;
    }

    public List<Team> getTeamListForClub(int clubId) throws SQLException {
        return getEntityList("clubid = " + clubId);
    }

    public List<Team> getTeamListForNoc(int nocId) throws SQLException {
        return getEntityList("nocid = " + nocId);
    }

    public List<Team> getTeamListForNocs(List<Integer> nocIds) throws SQLException {
        return new ArrayList<>() {{
            if (!nocIds.isEmpty())
                addAll(getEntityList("nocid IN (" + getCommaSepIntList(nocIds) + ")"));
        }};
    }

    public List<Team> getTeamListForEquipe(int equipeId) throws SQLException {
        return getEntityList("equipeid = " + equipeId);
    }
}
