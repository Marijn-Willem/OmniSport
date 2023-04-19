package com.sports.entity.manager;

import com.sports.entity.H2HMatchPartStat;
import com.sports.entity.key.CompSeasonParticipantKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.entity.key.H2HMatchPartStatKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class H2HMatchPartStatManager<S extends H2HMatchPartStatKey, T extends H2HMatchPartStat>
        extends SuperKeySuperManager<S, T> implements AbstractSuperKeyManager {
    public H2HMatchPartStatManager(Statement stat) {
        super(stat);
    }

    public String[] getGenericColumns() {
        return new String[] {
                "stattypeid",
                "\"value\""
            };
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", " + getIdColumn();
    }

    @Override
    String[] getValueColumns() {
        return Util.concatenateStringArrays(getSpecificValueColumns(), getGenericColumns());
    }

    public List<T> getH2HMatchPartStats(List<? extends H2HMatchPartKey> h2HMatchPartKeys,
                                        List<Integer> statTypeIds) throws SQLException {
        List<T> h2HMatchPartStats = new ArrayList<T>();

        if (h2HMatchPartKeys.size() > 0 && statTypeIds.size() > 0) {
            String query = "SELECT " + getSelectColumnString() + " FROM " + getTableName() +
                    " WHERE (" + getConditionsKeyList(h2HMatchPartKeys) + ") " +
                    "AND stattypeid IN (" + getCommaSepIntList(statTypeIds) + ")";

            ResultSet rs = stat.executeQuery(query);

            while (rs.next())
                h2HMatchPartStats.add(getInstanceFromResultSet(rs));
        }

        return h2HMatchPartStats;
    }

    public Map<S, T> getH2HMatchPartStatMap(
            List<? extends CompSeasonParticipantKey> compSeasonParticKeys) throws SQLException {
        return getSuperKeyEntityMapFromSuperKeys(compSeasonParticKeys);
    }

    public void insert(S key, T h2HMatchPartStat) throws SQLException {
        super.insert(key, h2HMatchPartStat);
    }

    public void update(S key, T h2HMatchPartStat) throws SQLException {
        super.update(key, h2HMatchPartStat);
    }

    public void updateMatchPartStatMap(Map<S, T> matchPartStatMap) throws SQLException {
        updateEntityMap(matchPartStatMap);
    }

    void fillGenericPropertiesFromResultSet(ResultSet rs, H2HMatchPartStat stat) throws SQLException {
        stat.setStatTypeId(rs.getInt("stattypeid"));
        stat.setValue(rs.getInt("value"));
    }
}
