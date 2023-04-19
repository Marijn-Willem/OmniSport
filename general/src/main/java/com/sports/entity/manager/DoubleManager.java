package com.sports.entity.manager;

import com.sports.entity.Double;
import com.sports.entity.key.DoublePersonSport1PersonSport2Key;
import com.sports.logic.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class DoubleManager extends ParticipantManager<Double> {
    public DoubleManager(Statement stat) {
        super(stat);
    }

    @Override
    String getTableName() {
        return "\"double\"";
    }

    @Override
    Double getInstanceFromResultSet(ResultSet rs) throws SQLException {
        Double dbl = new Double();

        fillGeneralPropertiesFromResultSet(rs, dbl);
        dbl.setPersonSport1Id(rs.getInt("personsport1id"));
        dbl.setPersonSport2Id(rs.getInt("personsport2id"));

        return dbl;
    }

    DoublePersonSport1PersonSport2Key getSuperKeyFromResultSet(ResultSet rs) throws SQLException {
        return new DoublePersonSport1PersonSport2Key(
                rs.getInt("personsport1id"), rs.getInt("personsport2id")
        );
    }

    String[] getSpecificValueColumns() {
        return new String[] {
                "personsport1id",
                "personsport2id"
            };
    }

    public Map<DoublePersonSport1PersonSport2Key, Double> getDoubleMapFromPersonSports(
            List<DoublePersonSport1PersonSport2Key> keys) throws SQLException {
        Map<DoublePersonSport1PersonSport2Key, Double> doubleMap = new HashMap<>();

        if (keys.size() > 0) {
            List<Double> doubleList = getDoubleList("(" + getConditionsKeyList(keys) + ")");

            for (Double dbl : doubleList)
                doubleMap.put(dbl.getPerson1Person2Key(), dbl);
        }

        return doubleMap;
    }

    public Map<DoublePersonSport1PersonSport2Key, Double> getDoublesWithPersonSport(int personSportId) throws SQLException {
        return getDoublesWithPersonSports(Collections.singletonList(personSportId));
    }

    public Map<DoublePersonSport1PersonSport2Key, Double> getDoublesWithPersonSports(List<Integer> personSportIds)
            throws SQLException {
        Map<DoublePersonSport1PersonSport2Key, Double> doubleMap = new HashMap<>();

        List<String> psQueries = personSportIds.stream().map(x -> getPersonSportQuery(x, "personsport1id"))
                .collect(Collectors.toList());
        psQueries.addAll(personSportIds.stream().map(x -> getPersonSportQuery(x, "personsport2id"))
                .collect(Collectors.toList()));

        String query = Util.concatStrings(psQueries, " UNION ");

        ResultSet rs = stat.executeQuery(query);

        while (rs.next())
            doubleMap.put(getSuperKeyFromResultSet(rs), getInstanceFromResultSet(rs));

        return doubleMap;
    }

    public int getNewId() throws SQLException {
        return super.getNewId();
    }

    public void insertDoubles(List<Double> doubleList, int idStart) throws SQLException {
        insertIdEntities(doubleList, idStart);
    }

    public Double getDouble(int doubleId) throws SQLException {
        return getEntityFromId(doubleId);
    }

    public void updateDoubleMap(Map<DoublePersonSport1PersonSport2Key, Double> doubleMap) throws SQLException {
        updateEntityMap(doubleMap);
    }

    private List<Double> getDoubleList(String whereClause) throws SQLException {
        return getEntityList(whereClause);
    }

    private String getPersonSportQuery(int personSportId, String personSportColumn) {
        return getGenericQuery(personSportColumn + " = " + personSportId);
    }
}
