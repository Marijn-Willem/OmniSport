package com.sports.entity;

public class AliasEntity extends NamedIntEntity {
    public static final int aliasEntityIdSport = -1;
    public static final int aliasEntityIdSportEvent = -2;
    public static final int aliasEntityIdSportDiscipline = -3;
    public static final int aliasEntityIdCompetition = -4;
    public static final int aliasEntityIdActionType = -5;
    public static final int aliasEntityIdStatType = -6;
    public static final int aliasEntityIdDisciplinePart = -7;
    public static final int aliasEntityIdSportEventPart = -8;
    public static final int aliasEntityIdPersonInstance = -9;
    public static final int aliasEntityIdPhaseType = -10;
    public static final int aliasEntityIdGeoInstance = -11;
    public static final int aliasEntityIdNocInstance = -12;
    public static final int aliasEntityIdClubInstance = -13;
    public static final int aliasEntityIdEventPartName = -14;
    public static final int aliasEntityIdLocationRole = -15;
    public static final int aliasEntityIdEquipeInstance = -16;
    public static final int aliasEntityIdResultType = -17;

    private int id;
    private String name;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] { name };
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
