package com.sports.entity.manager;

import com.sports.entity.CompSeasonPhaseTeam;
import com.sports.entity.key.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CompSeasonPhaseTeamManager extends CompSeasonPhaseParticipantManager<CompSeasonTeamKey,
        CompSeasonPhaseTeamKey, CompSeasonPhaseTeam> {
    public CompSeasonPhaseTeamManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "compseasonphaseteam";
    }

    @Override
    String getSpecificIdColumn() {
        return "teamid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "pointscorrection" };
    }

    @Override
    CompSeasonPhaseTeamKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new CompSeasonPhaseTeamKey(((CompSeasonPhaseManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getSpecificIdColumn()));
    }

    @Override
    CompSeasonPhaseTeam getInstanceFromResultSet(ResultSet rs) throws SQLException {
        CompSeasonPhaseTeam compSeasonPhaseTeam = new CompSeasonPhaseTeam();

        compSeasonPhaseTeam.setTeamId(rs.getInt("teamid"));
        compSeasonPhaseTeam.setPointsCorrection(rs.getInt("pointscorrection"));

        return compSeasonPhaseTeam;
    }

    @Override
    public void insert(CompSeasonPhaseParticipantKey key) throws SQLException {
        insert((CompSeasonPhaseTeamKey)key, new CompSeasonPhaseTeam());
    }

    @Override
    public void insertPhaseParticipantKeyList(List<? extends CompSeasonPhaseParticipantKey> keys) throws SQLException {
        Map<CompSeasonPhaseTeamKey, CompSeasonPhaseTeam> insertMap = new HashMap<CompSeasonPhaseTeamKey, CompSeasonPhaseTeam>() {{
            keys.forEach(k -> put((CompSeasonPhaseTeamKey)k, new CompSeasonPhaseTeam()));
        }};

        insert(insertMap);
    }

    public List<Integer> getTeamsInCompSeasonPhases(List<CompSeasonPhaseKey> keys)
            throws SQLException {
        return getParticipantsInCompSeasonPhases(keys);
    }

    public List<CompSeasonPhaseTeam> getCompSeasonPhaseTeams(List<CompSeasonPhaseTeamKey> keys) throws SQLException {
        return getEntityListFromSuperKeys(keys);
    }

    public Set<CompSeasonPhaseKey> getCompSeasonPhaseKeysTeam(int teamId) throws SQLException {
        Set<CompSeasonPhaseKey> compSeasonPhaseKeys = new HashSet<CompSeasonPhaseKey>();

        String query = getGenericQuery("teamid = " + teamId);

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            compSeasonPhaseKeys.add(getCompSeasonPhaseKeyFromResultSet(rs));

        return compSeasonPhaseKeys;
    }

    public List<CompSeasonPhaseKey> getCompSeasonPhaseKeysWithTeam(CompSeasonKey csk, int teamId)
        throws SQLException {
        List<CompSeasonPhaseKey> compSeasonPhaseKeys = new ArrayList<CompSeasonPhaseKey>();

        ResultSet rs = stat.executeQuery(getGenericQuery(csk.getWhereClause() + " AND teamid = " + teamId));

        while (rs.next())
            compSeasonPhaseKeys.add(getCompSeasonPhaseKeyFromResultSet(rs));

        return compSeasonPhaseKeys;
    }

    private CompSeasonPhaseKey getCompSeasonPhaseKeyFromResultSet(ResultSet rs) throws SQLException {
        return getSuperKeyFromResultSet(rs).getSuperKey();
    }
}
