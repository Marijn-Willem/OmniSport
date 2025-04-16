package com.sports.entity;

import com.sports.db.util.QueryUtil;

public class Sport extends IntAliasable {
    public static final int sportIdFootball = -1;
    public static final int sportIdRugby = -2;
    public static final int sportIdHockey = -3;
    public static final int sportIdSpeedSkating = -4;
    public static final int sportIdDarts = -5;
    public static final int sportIdCyclingRoad = -6;
    public static final int sportIdBasketball = -7;
    public static final int sportIdAmericanFootball = -8;

    public static final String sportNameFootball = "Football";
    public static final String sportNameRugby = "Rugby";
    public static final String sportNameHockey = "Hockey";
    public static final String sportNameBasketball = "Basketball";
    public static final String sportNameAmericanFootball = "American Football";
    public static final String sportNameCyclingRoad = "Cycling Road";

    private int id;
    private String name;
    private boolean isTeam;
    private boolean isH2H;
    private boolean hasMatchParts;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertBooleanToDbValue(isTeam),
                QueryUtil.convertBooleanToDbValue(isH2H),
                QueryUtil.convertBooleanToDbValue(hasMatchParts)
            };
    }

    public static String getNameFromId(int id) {
        return switch (id) {
            case sportIdFootball -> sportNameFootball;
            case sportIdRugby -> sportNameRugby;
            case sportIdHockey -> sportNameHockey;
            case sportIdBasketball -> sportNameBasketball;
            case sportIdAmericanFootball -> sportNameAmericanFootball;
            case sportIdCyclingRoad -> sportNameCyclingRoad;
            default -> null;
        };
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdSport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(boolean team) {
        isTeam = team;
    }

    public boolean isTeam() {
        return isTeam;
    }

    public void setH2H(boolean h2H) {
        isH2H = h2H;
    }

    public boolean isH2H() {
        return isH2H;
    }

    public boolean isHasMatchParts() {
        return hasMatchParts;
    }

    public void setHasMatchParts(boolean hasMatchParts) {
        this.hasMatchParts = hasMatchParts;
    }
}
