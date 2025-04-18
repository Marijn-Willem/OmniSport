package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.CompSeasonPhase;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.CompSeasonPhaseKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

public class CompSeasonPhaseManager extends SuperKeySuperManager<CompSeasonPhaseKey, CompSeasonPhase> {
    public CompSeasonPhaseManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasonphase";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", compseasonphaseid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
            "parentphaseid",
            "round",
            "knockoutparent",
            "bestof1",
            "bestof2",
            "bestofdec",
            "finished",
            "startdate",
            "enddate",
            "hasstanding",
            "parentorder",
            "expandfactor",
            "hasdivisionstandings",
            "phasetypeid"
        };
    }

    @Override
    CompSeasonPhase getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonPhase compSeasonPhase = new CompSeasonPhase();

        compSeasonPhase.setCompSeasonPhaseKey(getSuperKeyFromResultSet(rs));
        compSeasonPhase.setParentPhaseId(QueryUtil.getIntegerFromResultSet(rs, "parentphaseid"));
        compSeasonPhase.setRound(QueryUtil.getIntegerFromResultSet(rs, "round"));
        compSeasonPhase.setKnockoutParent(rs.getBoolean("knockoutparent"));
        compSeasonPhase.setBestOf1(QueryUtil.getIntegerFromResultSet(rs, "bestof1"));
        compSeasonPhase.setBestOf2(QueryUtil.getIntegerFromResultSet(rs, "bestof2"));
        compSeasonPhase.setBestOfDec(QueryUtil.getIntegerFromResultSet(rs, "bestofdec"));
        compSeasonPhase.setFinished(rs.getBoolean("finished"));
        compSeasonPhase.setStartDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("startdate")));
        compSeasonPhase.setEndDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("enddate")));
        compSeasonPhase.setHasStanding(rs.getBoolean("hasstanding"));
        compSeasonPhase.setParentOrder(QueryUtil.getIntegerFromResultSet(rs, "parentorder"));
        compSeasonPhase.setExpandFactor(QueryUtil.getIntegerFromResultSet(rs, "expandfactor"));
        compSeasonPhase.setHasDivisionStandings(rs.getBoolean("hasdivisionstandings"));
        compSeasonPhase.setPhaseTypeId(rs.getInt("phasetypeid"));

        return compSeasonPhase;
    }

    @Override
    CompSeasonPhaseKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPhaseKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("compseasonphaseid"));
    }

    @Override
    CompSeasonManager getSuperManager() {
        return new CompSeasonManager(stat);
    }   

    public List<CompSeasonPhase> getPhaseListFromParent(CompSeasonPhaseKey parentKey)
        throws SQLException {
        return getChildEntities(Collections.singletonList(parentKey));
    }

    public List<CompSeasonPhase> getNonFinishedPhasesFromParent(CompSeasonPhaseKey parentKey) throws SQLException {
        return getChildEntities(Collections.singletonList(parentKey), "NOT(finished)");
    }

    public List<CompSeasonPhase> getKnockoutCompSeasonPhases(CompSeasonKey csk) throws SQLException {
        return getCompSeasonPhases(csk, "knockoutparent");
    }

    public List<CompSeasonPhase> getNonKnockoutCompSeasonPhases(CompSeasonKey csk)
            throws SQLException {
        return getCompSeasonPhases(csk, "NOT(knockoutparent)");
    }

    public CompSeasonPhase getCompSeasonPhase(CompSeasonPhaseKey cskp) throws SQLException {
        return getEntityFromSuperKey(cskp);
    }

    public List<CompSeasonPhase> getCompSeasonPhases(CompSeasonKey compSeasonKey) throws SQLException {
        return getCompSeasonPhases(compSeasonKey, null);
    }

    public List<CompSeasonPhase> getCompSeasonPhases(Collection<CompSeasonPhaseKey> compSeasonPhaseKeys)
            throws SQLException {
        return getEntityListFromSuperKeys(compSeasonPhaseKeys);
    }

    public void updateCompSeasonPhase(CompSeasonPhaseKey key, CompSeasonPhase compSeasonPhase)
        throws SQLException {
        update(key, compSeasonPhase);
    }

    public List<CompSeasonPhaseKey> getCompSeasonPhasesAfterDate(List<Integer> compIds, LocalDateTime startDate)
            throws SQLException {
        String whereClause = "competitionid IN (" + getCommaSepIntList(compIds) + ") " +
                "AND startdate >= " + QueryUtil.convertDateTimeToDbString(startDate);

        return getSuperKeyList(whereClause);
    }

    public CompSeasonPhase getCompSeasonPhaseForRound(CompSeasonKey csk, int round) throws SQLException {
        List<CompSeasonPhase> compSeasonPhases = getCompSeasonPhases(csk, "round = " + round);

        return compSeasonPhases.size() == 1 ? compSeasonPhases.get(0) : null;
    }

    public void insertCompSeasonPhases(List<CompSeasonPhase> compSeasonPhases) throws SQLException {
        Map<CompSeasonPhaseKey, CompSeasonPhase> insertMap = new HashMap<>();

        for (CompSeasonPhase csp : compSeasonPhases)
            insertMap.put(csp.getCompSeasonPhaseKey(), csp);

        insert(insertMap);
    }

    public int getNewPhaseId(CompSeasonKey csk) throws SQLException {
        return getNewInt("compseasonphaseid", csk.getWhereClause());
    }

    public List<CompSeasonPhase> getChildCompSeasonPhases(List<CompSeasonPhaseKey> keys) throws SQLException {
        return getChildEntities(keys);
    }

    public List<CompSeasonPhase> getPhasesByCompSeasonAndPhaseTypes(CompSeasonKey compSeasonKey, List<Integer> phaseTypeIds)
            throws SQLException {
        return !phaseTypeIds.isEmpty() ?
                getEntityList(compSeasonKey.getWhereClause() + " AND phasetypeid IN (" + getCommaSepIntList(phaseTypeIds) + ")") :
                Collections.emptyList();
    }

    public void delete(CompSeasonPhaseKey cspk) throws SQLException {
        delete(Collections.singletonList(cspk));
    }

    private List<CompSeasonPhase> getCompSeasonPhases(CompSeasonKey csk, String whereClause) throws SQLException {
        String fullClause = csk.getWhereClause() + Util.getPrefixedStringOrEmptyString(whereClause, " AND ");

        return getEntityList(fullClause);
    }
}
