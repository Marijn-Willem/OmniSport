package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.Competition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CompetitionManager extends IntAliasableManager<Competition> {
    public CompetitionManager(Statement stat) {
        super(stat);
    }

    @Override
    Competition getInstance() {
        return new Competition();
    }

    @Override
    String getTableName() {
        return "competition";
    }

    @Override
    String[] getValueColumns() {
        return new String[]{ "name", "sportid", "cupteaminitid", "cupdateinit", "h2hdouble", "genderid",
                "isdomestic", "geoid" };
    }

    @Override
    Competition getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Competition competition = new Competition();

        competition.setId(rs.getInt("id"));
        competition.setName(rs.getString("name"));
        competition.setSportId(rs.getInt("sportid"));
        competition.setCupTeamInitId(QueryUtil.getIntegerFromResultSet(rs, "cupteaminitid"));
        competition.setCupDateInit(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("cupdateinit")));
        competition.setH2hDouble(rs.getBoolean("h2hdouble"));
        competition.setGenderId(rs.getInt("genderid"));
        competition.setDomestic(rs.getBoolean("isdomestic"));
        competition.setGeoId(QueryUtil.getIntegerFromResultSet(rs, "geoid"));

        return competition;
    }

    public List<Integer> getIdsForSport(int sportId) throws SQLException {
        return getIdList("sportid = " + sportId);
    }

    public Competition getCompetition(int id) throws SQLException {
        List<Competition> competitions = getCompetitionList(Collections.singletonList(id));

        if (competitions.size() == 1)
            return competitions.get(0);

        return null;
    }

    public List<Competition> getCompetitionList() throws SQLException {
        return getEntityList(null);
    }

    public List<Competition> getCompetitionList(int sportId) throws SQLException {
        return getEntityList("sportid = " + sportId);
    }

    public List<Competition> getCompetitionList(Collection<Integer> compIds) throws SQLException {
        return getEntityListFromIds(compIds);
    }

    public List<Competition> getCompetitionListNoH2HDouble() throws SQLException {
        return getEntityList("h2hdouble = " + QueryUtil.convertBooleanToDbValue(false));
    }

    public Map<String, Competition> getCompetitionMap(List<String> compNames, int sportId)
            throws SQLException {
        Map<String, Competition> compMap = new HashMap<String, Competition>();

        if (compNames.size() > 0) {
            String query = getGenericQuery("name IN (" +
                    getCommaSepStringList(compNames) + ") AND sportid = " + sportId);

            ResultSet rs = stat.executeQuery(query);

            while (rs.next())
                compMap.put(rs.getString("name"), getCompetitionFromResultSet(rs));
        }

        return compMap;
    }

    public void updateCompetition(int id, Competition competition) throws SQLException {
        update(id, competition);
    }

    public int getNewId() throws SQLException {
        return super.getNewId();
    }

    public void insertCompetition(int id, Competition competition) throws SQLException {
        insert(id, competition);
    }

    private Competition getCompetitionFromResultSet(ResultSet rs) throws SQLException {
        return (Competition)getInstanceFromResultSet(rs);
    }
}
