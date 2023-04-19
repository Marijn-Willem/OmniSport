package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.DoublesMatchPart;
import com.sports.entity.key.DoublesMatchPartKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DoublesMatchPartManager extends H2HMatchPartManager<DoublesMatchPartKey, DoublesMatchPart> {
    public DoublesMatchPartManager(Statement stat) {
        super(stat);
    }

    public String[] getSpecificValueColumns() {
        return new String[] { "double1win" };
    }

    public String getIdColumn() {
        return "doublesmatchpartid";
    }

    protected String getTableName() {
        return "doublesmatchpart";
    }

    @Override
    protected DoublesMatchPart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        DoublesMatchPart doublesMatchPart = new DoublesMatchPart();

        doublesMatchPart.setDoublesMatchPartId(rs.getInt("doublesmatchpartid"));
        doublesMatchPart.setName(rs.getString("name"));
        doublesMatchPart.setParentMatchPartId(QueryUtil.getIntegerFromResultSet(rs, "parentmatchpartid"));
        doublesMatchPart.setDouble1Win(rs.getBoolean("double1win"));
        doublesMatchPart.setFinished(rs.getBoolean("finished"));

        return doublesMatchPart;
    }

    @Override
    DoublesMatchPartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DoublesMatchPartKey(((DoublesMatchManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }

    @Override
    protected DoublesMatchManager getSuperManager() {
        return new DoublesMatchManager(stat);
    }
}
