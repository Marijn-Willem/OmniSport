package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.SportEventKey;

public class SportEvent extends SuperKeyAliasable implements GenderAliasable {
    public static final int sportEventIdCyclingRoadSingleMale = 1;
    public static final int sportEventIdCyclingRoadSingleFemale = 2;
    public static final int sportEventIdCyclingRoadStageMale = 5;
    public static final int sportEventIdCyclingRoadStageFemale = 6;
    public static final int sportEventIdCyclingRoadGeneralMale = 11;
    public static final int sportEventIdCyclingRoadGeneralFemale = 12;

    private String name;
    private boolean pointsSortAsc;
    private int genderId;
    private boolean isTeam;

    private int sportId;
    private int sportEventId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertStringToDbValue(name),
                QueryUtil.convertBooleanToDbValue(pointsSortAsc),
                String.valueOf(genderId),
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

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
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
