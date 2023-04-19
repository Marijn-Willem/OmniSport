package com.sports.entity.manager;

import com.sports.entity.DisciplinePart;
import com.sports.entity.key.DisciplinePartKey;
import com.sports.entity.key.SportDisciplineKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DisciplinePartManager extends SuperKeyAliasableManager<DisciplinePartKey, DisciplinePart> {
    public DisciplinePartManager(Statement stat) {
        super(stat);
    }

    @Override
    DisciplinePart getInstance() {
        return new DisciplinePart();
    }

    @Override
    String getTableName() {
        return "disciplinepart";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", disciplinepartid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] { "name", "\"order\"" };
    }

    @Override
    DisciplinePartKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DisciplinePartKey(((SportDisciplineManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("disciplinepartid"));
    }

    @Override
    DisciplinePart getInstanceFromResultSet(ResultSet rs) throws SQLException {
        DisciplinePart disciplinePart = new DisciplinePart();
        disciplinePart.setName(rs.getString("name"));
        disciplinePart.setOrder(rs.getInt("order"));
        disciplinePart.setSportDisciplineId(rs.getInt("sportdisciplineid"));
        disciplinePart.setDisciplinePartId(rs.getInt("disciplinepartid"));

        return disciplinePart;
    }

    @Override
    SportDisciplineManager getSuperManager() {
        return new SportDisciplineManager(stat);
    }

    public List<DisciplinePart> getDisciplinePartList(SportDisciplineKey sdk) throws SQLException {
        return getEntityList(sdk.getWhereClause());
    }

    public List<DisciplinePart> getDisciplinePartList(List<SportDisciplineKey> disciplineKeys) throws SQLException {
        return getEntityListFromSuperKeys(disciplineKeys);
    }

    public DisciplinePart getDisciplinePart(DisciplinePartKey dpk) throws SQLException {
        return getEntityFromSuperKey(dpk);
    }

    public int getNewDisciplinePartId(SportDisciplineKey sdk) throws SQLException {
        return getNewInt("disciplinepartid", sdk.getWhereClause());
    }
}
