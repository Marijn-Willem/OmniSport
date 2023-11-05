package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.H2HMatch;
import com.sports.entity.key.*;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class H2HMatchManager<S extends H2HMatchKey, T extends H2HMatch> extends SuperKeySuperManager<S, T>
        implements AbstractSuperKeyManager {
    protected abstract String getParticipant1IdColumn();

    protected abstract String getParticipant2IdColumn();

    protected abstract String getScore1Column();

    protected abstract String getScore2Column();

    protected abstract String getParticipant1NcrIdColumn();

    protected abstract String getParticipant2NcrIdColumn();

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", " + getIdColumn();
    }

    @Override
    String[] getValueColumns() {
        return Util.concatenateStringArrays(getSpecificValueColumns(), getGenericColumns());
    }

    @Override
    CompSeasonManager getSuperManager() {
        return new CompSeasonManager(stat);
    }

    public String[] getGenericColumns() {
        return new String[]{getParticipant1IdColumn(), getParticipant2IdColumn(),
                getScore1Column(), getScore2Column(),
                getParticipant1NcrIdColumn(), getParticipant2NcrIdColumn(),
                "finished", "compseasonphaseid", "knockoutorder", "\"date\""};
    }

    void fillGenericPropertiesFromResultSet(T h2HMatch, ResultSet rs) throws SQLException {
        h2HMatch.setParticipant1Id(QueryUtil.getIntegerFromResultSet(rs, getParticipant1IdColumn()));
        h2HMatch.setParticipant2Id(QueryUtil.getIntegerFromResultSet(rs, getParticipant2IdColumn()));
        h2HMatch.setScore1_1(QueryUtil.getIntegerFromResultSet(rs, getScore1Column()));
        h2HMatch.setScore1_2(QueryUtil.getIntegerFromResultSet(rs, getScore2Column()));
        h2HMatch.setParticipant1NcrId(QueryUtil.getIntegerFromResultSet(rs, getParticipant1NcrIdColumn()));
        h2HMatch.setParticipant2NcrId(QueryUtil.getIntegerFromResultSet(rs, getParticipant2NcrIdColumn()));
        h2HMatch.setFinished(rs.getBoolean("finished"));
        h2HMatch.setCompSeasonPhaseId(rs.getInt("compseasonphaseid"));
        h2HMatch.setKnockoutOrder(QueryUtil.getIntegerFromResultSet(rs, "knockoutorder"));
        h2HMatch.setDate(QueryUtil.convertTimestampToDateTime(rs.getTimestamp("date")));
        h2HMatch.setCompSeasonPhaseKey(new CompSeasonPhaseKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("compseasonphaseid")));
    }

    public H2HMatchManager(Statement stat) {
        super(stat);
    }

    public T getInstanceFromKey(S key) throws SQLException {
        String query = "SELECT " + getSelectColumnString() + " FROM " + getTableName() +
                " WHERE " + key.getWhereClause();

        ResultSet rs = stat.executeQuery(query);

        if (rs.next())
            return getInstanceFromResultSet(rs);

        return null;
    }

    public List<T> getH2HMatchList(CompSeasonKey csk, String whereClause)
            throws SQLException {
        String query = getGenericQuery(csk.getWhereClause() +
                Util.getPrefixedStringOrEmptyString(whereClause, " AND "));

        return getMatches(query);
    }

    public int getNewSpecId(CompSeasonKey csk) throws SQLException {
        return getNewInt(getIdColumn(), csk.getWhereClause());
    }

    public List<T> getH2HMatchesFromCompSeasonPhases(List<CompSeasonPhaseKey> compSeasonPhaseKeys)
            throws SQLException {
        return getEntityListFromSuperKeys(compSeasonPhaseKeys);
    }

    public void deleteMatchesFromCompSeasonPhases(List<CompSeasonPhaseKey> phaseKeys)
            throws SQLException {
        delete(phaseKeys);
    }

    public List<T> getNonFinishedH2HMatches(CompSeasonPhaseKey compSeasonPhaseKey)
            throws SQLException {
        List<T> h2HMatches = new ArrayList<T>();

        ResultSet rs = stat.executeQuery(getGenericQuery(compSeasonPhaseKey.getWhereClause() + " AND NOT(finished)"));

        while (rs.next())
            h2HMatches.add(getInstanceFromResultSet(rs));

        return h2HMatches;
    }

    public void update(S h2HMatchKey, T match) throws SQLException {
        super.update(h2HMatchKey, match);
    }

    public void updateMatchMap(Map<S, T> matchMap) throws SQLException {
        super.updateEntityMap(matchMap);
    }

    public void insertMatchMap(Map<S, T> matchMap) throws SQLException {
        super.insert(matchMap);
    }

    public List<T> getPlayedMatchesInCompSeasonPhase(CompSeasonPhaseKey cspk) throws SQLException {
        return getMatches(getPlayedMatchQuery(cspk.getWhereClause()));
    }

    public List<T> getMatchesInCompSeasonPhases(List<CompSeasonPhaseKey> phaseKeys) throws SQLException {
        return getEntityList(getConditionsKeyList(phaseKeys));
    }

    public List<T> getMatchesParticipantInCompSeason(CompSeasonParticipantKey cspk)
            throws SQLException {
        String query = getCompSeasonParticipantQuery(cspk, true) +
                " UNION " +
                getCompSeasonParticipantQuery(cspk, false);

        return getMatches(query);
    }

    public List<T> getMatchesForParticipants(List<CompSeasonParticipantKey> keyList) throws SQLException {
        return getEntityListFromSuperKeys(keyList);
    }

    public List<T> getMatchesWithBothParticipants(Collection<CompSeasonKey> compSeasonKeys, int participant1Id, int participant2Id)
        throws SQLException {
        return new ArrayList<>() {{
            if (!compSeasonKeys.isEmpty()) {
                StringBuilder sb = new StringBuilder();

                compSeasonKeys.forEach(x -> {
                    if (!sb.isEmpty())
                        sb.append(" OR ");

                    sb.append("(");
                    sb.append(getMatchWithBothParticipantsClause(x, participant1Id, participant2Id));
                    sb.append(")");
                });

                addAll(getEntityList(sb.toString()));
            }
        }};
    }

    public Map<S, T> getMatchesForPhaseParticipants(
            List<? extends CompSeasonPhaseParticipantKey> keyList) throws SQLException {
        Map<S, T> matchMap = new HashMap<S, T>();

        if (!keyList.isEmpty())
            matchMap = getSuperKeyEntityMap("(" +
                    getCompSeasonPhaseParticQuery(keyList, getParticipant1IdColumn()) + " OR " +
                    getCompSeasonPhaseParticQuery(keyList, getParticipant2IdColumn()) + ")");

        return matchMap;
    }

    String getPlayedMatchQuery(String whereClauseSuppl) {
        return getGenericQuery(getScore1Column() + " IS NOT NULL AND " + whereClauseSuppl);
    }

    String getSelectColumnString() {
        return getKeyColumnString() + ", " + Util.concatStrings(getValueColumns(), ", ");
    }

    private List<T> getMatches(String query) throws SQLException {
        List<T> playedTeamMatches = new ArrayList<T>();

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            playedTeamMatches.add(getInstanceFromResultSet(rs));

        return playedTeamMatches;
    }

    private String getCompSeasonParticipantQuery(CompSeasonParticipantKey cspk, boolean isPartic1) {
        return getGenericQuery(cspk.getSuperKey().getWhereClause() + " AND " +
                (isPartic1 ? getParticipant1IdColumn() : getParticipant2IdColumn()) +
                " = " + cspk.getSpecificId());
    }

    private String getCompSeasonPhaseParticQuery(List<? extends CompSeasonPhaseParticipantKey> keyList, String colName) {
        String[] subConditions = new String[keyList.size()];

        for (int i = 0; i < keyList.size(); i++) {
            CompSeasonPhaseParticipantKey key = keyList.get(i);

            String subQuery = "(" + key.getSuperKey().getWhereClause() + " AND " +
                    colName + " = " + key.getSpecificId() + ")";
            subConditions[i] = subQuery;
        }

        return Util.concatStrings(subConditions, " OR ");
    }

    private String getMatchWithBothParticipantsClause(CompSeasonKey csKey, int participant1Id, int participant2Id) {
        return "(" + csKey.getWhereClause() + " AND " +
                getParticipant1IdColumn() + " = " + participant1Id + " AND " +
                getParticipant2IdColumn() + " = " + participant2Id + ") OR (" +
                csKey.getWhereClause() + " AND " +
                getParticipant1IdColumn() + " = " + participant2Id + " AND " +
                getParticipant2IdColumn() + " = " + participant1Id + ")";
    }
}
