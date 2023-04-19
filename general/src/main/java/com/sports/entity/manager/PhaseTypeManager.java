package com.sports.entity.manager;

import com.sports.db.util.QueryUtil;
import com.sports.entity.PhaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class PhaseTypeManager extends IntAliasableManager<PhaseType> {
    public PhaseTypeManager(Statement stat) {
        super(stat);
    }

    @Override
    PhaseType getInstance() {
        return new PhaseType();
    }

    @Override
    String getTableName() {
        return "phasetype";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
                "name",
                "parentid",
                "isparent"
        };
    }

    public Map<Integer, PhaseType> getPhaseTypeMap(List<Integer> idList) throws SQLException {
        return getEntityMapFromIds(idList);
    }

    public List<PhaseType> getPhaseTypeList() throws SQLException {
        return getEntityList(null);
    }

    public List<PhaseType> getParentPhaseTypeList() throws SQLException {
        return getEntityList("isparent");
    }

    public List<PhaseType> getParentPhaseTypeListWithoutId(int idWithout) throws SQLException {
        return getEntityList("isparent AND id <> " + idWithout);
    }

    public List<PhaseType> getNonParentPhaseTypeList() throws SQLException {
        return getEntityList("NOT(isparent)");
    }

    public List<Integer> getChildIds(int phaseTypeId) throws SQLException {
        return getIdList("parentid = " + phaseTypeId);
    }

    @Override
    PhaseType getInstanceFromResultSet(ResultSet rs) throws SQLException {
        PhaseType phaseType = new PhaseType();

        phaseType.setId(rs.getInt("id"));
        phaseType.setName(rs.getString("name"));
        phaseType.setParentId(QueryUtil.getIntegerFromResultSet(rs, "parentid"));
        phaseType.setParent(rs.getBoolean("isparent"));

        return phaseType;
    }
}
