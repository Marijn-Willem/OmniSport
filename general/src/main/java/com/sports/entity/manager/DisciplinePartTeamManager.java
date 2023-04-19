package com.sports.entity.manager;

import com.sports.entity.DisciplinePartTeam;
import com.sports.entity.key.DisciplinePartTeamKey;
import com.sports.entity.key.EventDisciplinePartKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DisciplinePartTeamManager extends AlcifoPartParticipantManager<DisciplinePartTeamKey, EventDisciplinePartKey, DisciplinePartTeam> {
    public DisciplinePartTeamManager(Statement stat) {
        super(stat);
    }

    @Override
    String getParticipantIdColumn() {
        return "teamid";
    }

    @Override
    public String[] getSpecificValueColumns() {
        return new String[0];
    }

    @Override
    DisciplinePartTeamKey getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DisciplinePartTeamKey(((EventPartTeamManager)getCachedSuperManager()).getSuperKeyFromResultSet(rs),
                rs.getInt("eventdisciplinepartid"));
    }

    @Override
    String getTableName() {
        return "disciplinepartteam";
    }

    @Override
    String getKeyColumnString() {
        return getCachedSuperManager().getKeyColumnString() + ", eventdisciplinepartid";
    }

    @Override
    DisciplinePartTeam getInstanceFromResultSet(ResultSet rs) throws SQLException {
        DisciplinePartTeam disciplinePartTeam = new DisciplinePartTeam();

        fillGenericPropertiesFromResultSet(disciplinePartTeam, rs);

        return disciplinePartTeam;
    }

    @Override
    EventPartTeamManager getSuperManager() {
        return new EventPartTeamManager(stat);
    }
}
