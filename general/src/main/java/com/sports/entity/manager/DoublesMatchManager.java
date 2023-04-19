package com.sports.entity.manager;

import com.sports.entity.DoublesMatch;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.key.DoublesMatchKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DoublesMatchManager extends H2HMatchManager<DoublesMatchKey, DoublesMatch> {
    public DoublesMatchManager(Statement stat) {
        super(stat);
    }

    public String[] getSpecificValueColumns() {
        return new String[] {
                "double1start"
            };
    }

    protected String getTableName() {
        return "doublesmatch";
    }

    public String getIdColumn() {
        return "doublesmatchid";
    }

    protected String getParticipant1IdColumn() {
        return "double1id";
    }

    protected String getParticipant2IdColumn() {
        return "double2id";
    }

    protected String getScore1Column() {
        return "double1score";
    }

    protected String getScore2Column() {
        return "double2score";
    }

    @Override
    protected String getParticipant1NcrIdColumn() {
        return "double1ncrid";
    }

    @Override
    protected String getParticipant2NcrIdColumn() {
        return "double2ncrid";
    }

    public List<DoublesMatch> getDoublesMatchList(CompSeasonKey compSeasonKey, String whereClause)
            throws SQLException {
        return getH2HMatchList(compSeasonKey, whereClause);
    }

    @Override
    protected DoublesMatch getInstanceFromResultSet(ResultSet rs) throws SQLException {
        DoublesMatch doublesMatch = new DoublesMatch();

        fillGenericPropertiesFromResultSet(doublesMatch, rs);
        doublesMatch.setDoublesMatchId(rs.getInt("doublesmatchid"));
        doublesMatch.setDouble1Start(rs.getBoolean("double1start"));

        return doublesMatch;
    }

    @Override
    DoublesMatchKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DoublesMatchKey(((CompSeasonManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt(getIdColumn()));
    }
}
