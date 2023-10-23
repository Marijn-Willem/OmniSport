package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.CompSeason;
import com.sports.entity.key.CompSeasonKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompSeasonManager extends SuperKeySuperManager<CompSeasonKey, CompSeason> {
    public CompSeasonManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseason";
    }

    @Override
    String getKeyColumnString() {
        return "competitionid, seasonid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "triesabsbonus", "triesrelbonus", "lossdiffbonus", "startdate", "enddate" };
    }

    @Override
    CompSeason getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeason compSeason = new CompSeason();

        compSeason.setCompetitionId(rs.getInt("competitionid"));
        compSeason.setSeasonId(rs.getInt("seasonid"));
        compSeason.setTriesAbsBonus(QueryUtil.getIntegerFromResultSet(rs, "triesabsbonus"));
        compSeason.setTriesRelBonus(QueryUtil.getIntegerFromResultSet(rs, "triesrelbonus"));
        compSeason.setLossDiffBonus(QueryUtil.getIntegerFromResultSet(rs, "lossdiffbonus"));
        compSeason.setStartDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("startdate")));
        compSeason.setEndDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("enddate")));

        return compSeason;
    }

    @Override
    CompSeasonKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonKey(rs.getInt("competitionid"), rs.getInt("seasonid"));
    }

    public Map<CompSeasonKey, CompSeason> getCompSeasonMapForSeason(List<Integer> compIds, int seasonId) throws SQLException {
        return new HashMap<>() {{
            if (!compIds.isEmpty())
                putAll(getSuperKeyEntityMap("competitionid IN (" + getCommaSepIntList(compIds) +
                        ") AND seasonid = " + seasonId));
        }};
    }

    public Map<CompSeasonKey, CompSeason> getCompSeasonMapForCompetition(int competitionId) throws SQLException {
        return getSuperKeyEntityMap("competitionid = " + competitionId);
    }

    public List<Integer> getSeasonIdsForCompetition(int competitionId) throws SQLException {
        return getIdList(getGenericQuery("competitionid = " + competitionId), "seasonid");
    }

    public CompSeason getCompSeason(CompSeasonKey csk) throws SQLException {
        return getEntityFromSuperKey(csk);
    }

    public void insertCompSeason(CompSeasonKey csk, CompSeason compSeason) throws SQLException {
        insert(csk, compSeason);
    }
}
