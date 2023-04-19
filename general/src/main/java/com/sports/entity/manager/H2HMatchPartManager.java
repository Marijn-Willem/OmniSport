package com.sports.entity.manager;

import com.sports.entity.H2HMatchPart;
import com.sports.entity.key.H2HMatchKey;
import com.sports.entity.key.H2HMatchPartKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class H2HMatchPartManager<S extends H2HMatchPartKey, T extends H2HMatchPart> extends SuperKeySuperManager<S, T>
        implements AbstractSuperKeyManager {
    public H2HMatchPartManager(Statement stat) {
        super(stat);
    }

    public String[] getGenericColumns() {
        return new String[]{
                "name",
                "parentmatchpartid",
                "finished"
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

    protected String getParticipant1IdColumn() {
        return null;
    }

    protected String getParticipant2IdColumn() {
        return null;
    }

    protected String getParticipant1WinColumn() {
        return null;
    }

    protected String getScore1Column() {
        return null;
    }

    protected String getScore2Column() {
        return null;
    }

    public T getH2HMatchPart(S h2HMatchPartKey) throws SQLException {
        String query = "SELECT " + getSelectColumnString() + " FROM " + getTableName() +
                " WHERE " + h2HMatchPartKey.getWhereClause();

        ResultSet rs = stat.executeQuery(query);

        if (rs.next())
            return getInstanceFromResultSet(rs);

        return null;
    }

    public <K extends H2HMatchKey> List<T> getH2HMatchPartsWithoutParent(K h2HMatchKey) throws SQLException {
        List<T> h2HMatchParts = new ArrayList<T>();

        String query = "SELECT " + getSelectColumnString() + " FROM " + getTableName() +
                " WHERE " + h2HMatchKey.getWhereClause() + " AND parentmatchpartid IS NULL";

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            h2HMatchParts.add(getInstanceFromResultSet(rs));

        return h2HMatchParts;
    }


    public List<T> getH2HMatchPartsFromParents(List<S> h2HMatchPartKeys)
            throws SQLException {
        List<T> h2HMatchParts = new ArrayList<T>();

        if (h2HMatchPartKeys.size() > 0) {
            String query = "SELECT " + getSelectColumnString() + " FROM " + getTableName() +
                    " WHERE (" + getConditionsKeyListParent(h2HMatchPartKeys) + ")";

            ResultSet rs = stat.executeQuery(query);

            while (rs.next())
                h2HMatchParts.add(getInstanceFromResultSet(rs));
        }

        return h2HMatchParts;
    }

    public void insert(S h2HMatchPartKey, T h2HMatchPart) throws SQLException {
        super.insert(h2HMatchPartKey, h2HMatchPart);
    }
}
