package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sport extends IntAliasable {
    public static final int sportIdFootball = 1;
    public static final int sportIdRugby = 2;
    public static final int sportIdHockey = 3;
    public static final int sportIdSpeedSkating = 5;
    public static final int sportIdDarts = 6;
    public static final int sportIdCyclingRoad = 9;
    public static final int sportIdBasketball = 10;
    public static final int sportIdAmericanFootball = 11;

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
        switch (id) {
            case sportIdFootball: return sportNameFootball;
            case sportIdRugby: return sportNameRugby;
            case sportIdHockey: return sportNameHockey;
            case sportIdBasketball: return sportNameBasketball;
            case sportIdAmericanFootball: return sportNameAmericanFootball;
            case sportIdCyclingRoad: return sportNameCyclingRoad;
            default: return null;
        }
    }

    public static int getIdFromName(String sportName) {
        if (sportNameFootball.equals(sportName))
            return sportIdFootball;
        else if (sportNameRugby.equals(sportName))
            return sportIdRugby;
        else if (sportNameHockey.equals(sportName))
            return sportIdHockey;
        else if (sportNameBasketball.equals(sportName))
            return sportIdBasketball;
        else if (sportNameAmericanFootball.equals(sportName))
            return sportIdAmericanFootball;
        else if (sportNameCyclingRoad.equals(sportName))
            return sportIdCyclingRoad;

        return 0;
    }

    public static List<Integer> getActionTypeIdsGoals(int id) {
        switch (id) {
            case sportIdFootball: return Collections.singletonList(ActionType.actionTypeIdGoal);
            case sportIdHockey: return Arrays.asList(ActionType.actionTypeIdPenalty,
                    ActionType.actionTypeIdFieldgoal, ActionType.actionTypeIdPencorner);
            default: return new ArrayList<Integer>();
        }
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
