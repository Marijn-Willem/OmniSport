package com.sports.entity;

import com.sports.db.type.Point;
import com.sports.db.util.QueryUtil;

public class EventPartLocation extends SuperKeyEntity implements DescribedEntity {
    private Integer geoId;
    private Point coordinates;
    private int locationRoleId;

    private int compSeasonEventPartId;
    private int eventPartLocationId;
    private String roleName;
    private String description;

    @Override
    public String[] getPropertiesInSQLStrings() {
        return new String[] {
                QueryUtil.convertIntegerToDbValue(geoId),
                QueryUtil.convertPointToDbValue(coordinates),
                "" + locationRoleId
        };
    }

    public int getCompSeasonEventPartId() {
        return compSeasonEventPartId;
    }

    public void setCompSeasonEventPartId(int compSeasonEventPartId) {
        this.compSeasonEventPartId = compSeasonEventPartId;
    }

    public int getEventPartLocationId() {
        return eventPartLocationId;
    }

    public void setEventPartLocationId(int eventPartLocationId) {
        this.eventPartLocationId = eventPartLocationId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGeoId() {
        return geoId;
    }

    public void setGeoId(Integer geoId) {
        this.geoId = geoId;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public int getLocationRoleId() {
        return locationRoleId;
    }

    public void setLocationRoleId(int locationRoleId) {
        this.locationRoleId = locationRoleId;
    }
}
