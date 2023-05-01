package com.sports.entity;

import com.sports.db.util.QueryUtil;

import java.time.LocalDateTime;

public class Competition extends IntAliasable implements Aliasable {
    private int id;
    private String name;
    private int sportId;
    private Integer cupTeamInitId;
    private LocalDateTime cupDateInit;
    private boolean h2hDouble;
    private int genderId;
    private boolean isDomestic;
    private Integer geoId;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
            QueryUtil.convertStringToDbValue(name),
            "" + sportId,
            QueryUtil.convertIntegerToDbValue(cupTeamInitId),
            QueryUtil.convertDateTimeToDbString(cupDateInit),
            QueryUtil.convertBooleanToDbValue(h2hDouble),
            "" + genderId,
            QueryUtil.convertBooleanToDbValue(isDomestic),
            QueryUtil.convertIntegerToDbValue(geoId)
        };
    }

    public int getAliasEntityId() {
        return AliasEntity.aliasEntityIdCompetition;
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

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public Integer getCupTeamInitId() {
        return cupTeamInitId;
    }

    public void setCupTeamInitId(Integer cupTeamInitId) {
        this.cupTeamInitId = cupTeamInitId;
    }

    public LocalDateTime getCupDateInit() {
        return cupDateInit;
    }

    public void setCupDateInit(LocalDateTime cupDateInit) {
        this.cupDateInit = cupDateInit;
    }

    public boolean isH2hDouble() {
        return h2hDouble;
    }

    public void setH2hDouble(boolean h2hDouble) {
        this.h2hDouble = h2hDouble;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public boolean isDomestic() {
        return isDomestic;
    }

    public void setDomestic(boolean domestic) {
        isDomestic = domestic;
    }

    public Integer getGeoId() {
        return geoId;
    }

    public void setGeoId(Integer geoId) {
        this.geoId = geoId;
    }
}
