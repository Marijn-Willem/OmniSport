package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.SportEventKey;

public class SportEvent extends SuperKeyAliasable implements Aliasable {
    public static final int sportEventIdCyclingRoadSingle = 1;
    public static final int sportEventIdCyclingRoadStage = 5;
    public static final int sportEventIdCyclingRoadGeneral = 11;
    public static final int sportEventIdCyclingRoadStageTeam = 13;
    public static final int sportEventIdSpeedSkatingBigOverall = 1;
    public static final int sportEventIdSpeedSkatingSmallOverall = 2;
    public static final int sportEventIdSpeedSkatingSprintOverall = 3;

    private String name;
    private boolean pointsSortAsc;
    private boolean isTeam;

    private int sportId;
    private int sportEventId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertBooleanToDbValue(pointsSortAsc),
                QueryUtil.convertBooleanToDbValue(isTeam)
            };
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdSportEvent;
    }

    public SportEventKey getSportEventKey() {
        return new SportEventKey(sportId, sportEventId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPointsSortAsc() {
        return pointsSortAsc;
    }

    public void setPointsSortAsc(boolean pointsSortAsc) {
        this.pointsSortAsc = pointsSortAsc;
    }

    public boolean isTeam() {
        return isTeam;
    }

    public void setTeam(boolean team) {
        isTeam = team;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getSportEventId() {
        return sportEventId;
    }

    public void setSportEventId(int sportEventId) {
        this.sportEventId = sportEventId;
    }
}
