package com.sports.entity.manager;

import com.sports.entity.DoublesMatchPartStat;
import com.sports.entity.key.DoublesMatchPartStatKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DoublesMatchPartStatManager extends H2HMatchPartStatManager<DoublesMatchPartStatKey, DoublesMatchPartStat> {
    public DoublesMatchPartStatManager(Statement stat) {
        super(stat);
    }

    public String[] getSpecificValueColumns() {
        return new String[] { "doubleid" };
    }

    @Override
    protected String getTableName() {
        return "doublesmatchpartstat";
    }

    public String getIdColumn() {
        return "doublesmatchpartstatid";
    }

    @Override
    protected DoublesMatchPartStat getInstanceFromResultSet(ResultSet rs) throws SQLException {
        DoublesMatchPartStat dmps = new DoublesMatchPartStat();

        fillGenericPropertiesFromResultSet(rs, dmps);
        dmps.setDoublesMatchPartId(rs.getInt("doublesmatchpartid"));
        dmps.setDoublesMatchPartStatId(rs.getInt("doublesmatchpartstatid"));
        dmps.setDoubleId(rs.getInt("doubleid"));

        return dmps;
    }

    @Override
    DoublesMatchPartStatKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DoublesMatchPartStatKey(((DoublesMatchPartManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    protected DoublesMatchPartManager getSuperManager() {
        return new DoublesMatchPartManager(stat);
    }
}
