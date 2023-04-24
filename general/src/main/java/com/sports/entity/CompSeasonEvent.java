package com.sports.entity;

import com.sports.db.util.QueryUtil;
import com.sports.entity.key.SportEventKey;

public class CompSeasonEvent extends SuperKeyEntity {
    private int sportId;
    private int sportEventId;
    private String externalSource;

    private int compSeasonEventId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                String.valueOf(sportId),
                String.valueOf(sportEventId),
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
}
