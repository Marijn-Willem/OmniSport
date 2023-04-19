package com.cyclingroad.servlet.ajax;

import com.sports.calc.cyclingroad.DbCalculation;
import com.sports.entity.key.CompSeasonEventPartKey;
import com.sports.entity.key.SuperKey;

import java.sql.SQLException;
import java.sql.Statement;

public class ProcessManageEventPartPersonSports extends com.sportservlet.ajax.ProcessManageEventPartPersonSports {
    @Override
    protected void postProcessInsertOrUpdate(Statement stat, SuperKey partKey) throws SQLException {
        new DbCalculation(stat).updateEventPersonSportsWithNoCountResult(((CompSeasonEventPartKey)partKey).getSuperKey());
    }
}
