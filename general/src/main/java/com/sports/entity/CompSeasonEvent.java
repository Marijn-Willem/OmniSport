package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.SportEventKey;

public class CompSeasonEvent extends SuperKeyEntity {
    private int sportId;
    private int sportEventId;
    private int genderId;
    private String externalSource;

    private int compSeasonEventId;
    private String sportEventName;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(sportId),
                String.valueOf(sportEventId),
                String.valueOf(genderId),
                QueryUtil.convertStringToDbValue(externalSource)
        };
    }

    public SportEventKey getSportEventKey() {
        return new SportEventKey(sportId, sportEventId);
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public void setSportEventId(int sportEventId) {
        this.sportEventId = sportEventId;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    public int getCompSeasonEventId() {
        return compSeasonEventId;
    }

    public void setCompSeasonEventId(int compSeasonEventId) {
        this.compSeasonEventId = compSeasonEventId;
    }

    public String getSportEventName() {
        return sportEventName;
    }

    public void setSportEventName(String sportEventName) {
        this.sportEventName = sportEventName;
    }
}
