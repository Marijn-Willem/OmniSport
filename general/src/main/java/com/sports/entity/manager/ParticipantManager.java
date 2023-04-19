package com.sports.entity.manager;

import com.sports.entity.Participant;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParticipantManager<T extends Participant> extends IntSuperManager<T> {
    public ParticipantManager(Statement stat) {
        super(stat);
    }

    abstract String[] getSpecificValueColumns();

    @Override
    String[] getValueColumns() {
        String[] generalValCols = new String[] { "description", "elo" };

        return Util.concatenateStringArrays(generalValCols, getSpecificValueColumns());
    }

    public Map<Integer, T> getParticipantMap(List<Integer> idList) throws SQLException {
        return getEntityMapFromIds(idList);
    }

    public List<T> getParticipantList(List<Integer> idList) throws SQLException {
        return getEntityListFromIds(idList);
    }

    public void updateParticipantMap(Map<Integer, ? extends Participant> particMap) throws SQLException {
        update(particMap);
    }

    public void delete(int id) throws SQLException {
        super.delete(id);
    }

    public Map<String, T> getDescriptionParticipantMap(List<String> descriptions) throws SQLException {
        Map<String, T> map = new HashMap<String, T>();

        ResultSet rs = stat.executeQuery(getGenericQuery("description IN ("+
                getCommaSepStringList(descriptions) + ")"));

        while (rs.next())
            map.put(rs.getString("description"), getInstanceFromResultSet(rs));

        return map;
    }

    void fillGeneralPropertiesFromResultSet(ResultSet rs, Participant part) throws SQLException {
        part.setId(rs.getInt("id"));
        part.setDescription(rs.getString("description"));
        part.setElo(rs.getInt("elo"));
    }
}
