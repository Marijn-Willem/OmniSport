package com.sports.entity.manager;

import com.sports.entity.ResultTypePrecision;
import com.sports.entity.key.SuperKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ResultTypePrecisionManager extends SuperKeySuperManager<SuperKey, ResultTypePrecision> {
    public ResultTypePrecisionManager(Statement stat) {
        super(stat);
    }

    @Override
    SuperKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    String getTableName() {
        return "resulttypeprecision";
    }

    @Override
    String getKeyColumnString() {
        return "resulttypeid, resulttypeprecisionid";
    }

    @Override
    String[] getValueColumns() {
        return new String[] {
            "name"
        };
    }

    public List<ResultTypePrecision> getPrecisionsForResultType(int resultTypeId) throws SQLException {
        return getEntityList("resulttypeid = " + resultTypeId);
    }

    @Override
    ResultTypePrecision getInstanceFromResultSet(ResultSet rs) throws SQLException {
        ResultTypePrecision resultTypePrecision = new ResultTypePrecision();

        resultTypePrecision.setResultTypePrecisionId(rs.getInt("resulttypeprecisionid"));
        resultTypePrecision.setName(rs.getString("name"));

        return resultTypePrecision;
    }
}
