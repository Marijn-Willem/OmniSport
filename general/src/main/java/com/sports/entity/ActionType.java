package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.util.HashMap;
import java.util.Map;

public class ActionType extends IntAliasable {
    public static final int actionTypeIdGoal = -1;
    public static final int actionTypeIdOwnGoal = -2;
    public static final int actionTypeIdTry = -3;
    public static final int actionTypeIdConversion = -4;
    public static final int actionTypeIdPenalty = -5;
    public static final int actionTypeIdDropgoal = -6;
    public static final int actionTypeIdFieldgoal = -7;
    public static final int actionTypeIdPencorner = -8;
    public static final int actionTypeIdPenaltyTry5 = -9;
    public static final int actionTypeIdPenaltyTry7 = -10;

    private static final String actionTypeNameGoal = "Goal";
    private static final String actionTypeNameOwnGoal = "Own goal";
    private static final String actionTypeNameTry = "Try";
    private static final String actionTypeNameConversion = "Conversion";
    private static final String actionTypeNamePenalty = "Penalty";
    private static final String actionTypeNameDropgoal = "Drop goal";
    private static final String actionTypeNameFieldgoal = "Field goal";
    private static final String actionTypeNamePencorner = "Penalty corner";
    private static final String actionTypeNamePenaltyTry5 = "Penalty try (5)";
    private static final String actionTypeNamePenaltyTry7 = "Penalty try (7)";

    public static final Map<String, Integer> nameIdMap = new HashMap<String, Integer>() {{
        put(actionTypeNameGoal, actionTypeIdGoal);
        put(actionTypeNameOwnGoal, actionTypeIdOwnGoal);
        put(actionTypeNameTry, actionTypeIdTry);
        put(actionTypeNameConversion, actionTypeIdConversion);
        put(actionTypeNamePenalty, actionTypeIdPenalty);
        put(actionTypeNameDropgoal, actionTypeIdDropgoal);
        put(actionTypeNameFieldgoal, actionTypeIdFieldgoal);
        put(actionTypeNamePencorner, actionTypeIdPencorner);
        put(actionTypeNamePenaltyTry5, actionTypeIdPenaltyTry5);
        put(actionTypeNamePenaltyTry7, actionTypeIdPenaltyTry7);
    }};

    private String name;

    private int id;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[]{
            QueryUtil.convertStringToDbValue(name)
        };
    }

    public static String getNameFromId(int id) {
        switch (id) {
            case actionTypeIdGoal: return actionTypeNameGoal;
            case actionTypeIdOwnGoal: return actionTypeNameOwnGoal;
            case actionTypeIdTry: return actionTypeNameTry;
            case actionTypeIdConversion: return actionTypeNameConversion;
            case actionTypeIdPenalty: return actionTypeNamePenalty;
            case actionTypeIdDropgoal: return actionTypeNameDropgoal;
            case actionTypeIdFieldgoal: return actionTypeNameFieldgoal;
            case actionTypeIdPencorner: return actionTypeNamePencorner;
            case actionTypeIdPenaltyTry5: return actionTypeNamePenaltyTry5;
            case actionTypeIdPenaltyTry7: return actionTypeNamePenaltyTry7;
            default: return null;
        }
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdActionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
