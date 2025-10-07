package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.SportEventPart;
import com.sports.entity.key.SportEventKey;
import com.sports.entity.key.SportEventPartKey;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SportEventPartManager extends SuperKeyAliasableManager<SportEventPartKey, SportEventPart> {
    public SportEventPartManager(Statement stat) {
        super(stat);
    }

    @Override
    SportEventPart getInstance() {
        return new SportEventPart();
    }

    @Override
    String getTableName() {
        return "sporteventpart";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", sporteventpartid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "sportdisciplineid",
                "name",
                "\"order\"",
                "weight",
                "isfinal"
            };
    }

    @Override
    SportEventPartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new SportEventPartKey(((SportEventManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("sporteventpartid"));
    }

    @Override
    SportEventPart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        SportEventPart sep = new SportEventPart();
        sep.setSportDisciplineId(rs.getInt("sportdisciplineid"));
        sep.setName(rs.getString("name"));
        sep.setOrder(rs.getInt("order"));
        sep.setSportEventPartId(rs.getInt("sporteventpartid"));
        sep.setWeight(QueryUtil.getIntegerFromResultSet(rs, "weight"));
        sep.setFinal(rs.getBoolean("isfinal"));

        return sep;
    }

    @Override
    SportEventManager getSuperManager() {
        return new SportEventManager(stat);
    }

    public SportEventPart getSportEventPart(SportEventPartKey sepk) throws SQLException {
        return getEntityFromSuperKey(sepk);
    }

    public SportEventPart getSportEventPartByOrder(SportEventKey sek, int order) throws SQLException {
        List<SportEventPart> sportEventParts = getSportEventParts(sek, "\"order\" = " + order);

        return sportEventParts.size() == 1 ? sportEventParts.get(0) : null;
    }

    public List<SportEventPart> getSportEventParts(SportEventKey sek) throws SQLException {
        return getSportEventParts(sek, null);
    }

    public List<SportEventPart> getSportEventParts(SportEventKey sek, String whereClauseSuppl)
        throws SQLException {
        String whereClause =
                Util.concatStringsWithDelimiter(sek.getWhereClause(), whereClauseSuppl, " AND ");

        return getEntityList(whereClause);
    }

    public List<SportEventPartKey> getSportEventPartKeys(List<SportEventKey> sportEventKeys) throws SQLException {
        return getSuperKeyList(getConditionsKeyList(sportEventKeys));
    }

    public int getNewPartId(SportEventKey sek) throws SQLException {
        return getNewInt("sporteventpartid", sek.getWhereClause());
    }

    public void update(SportEventPartKey sepk, SportEventPart sep) throws SQLException {
        super.update(sepk, sep);
    }

    public void insert(SportEventPartKey sepk, SportEventPart sep) throws SQLException {
        super.insert(sepk, sep);
    }
}
